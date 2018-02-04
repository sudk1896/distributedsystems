package it.unisa.sisd.socialnetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unisa.sisd.bean.User;
import it.unisa.sisd.question.QuestionsLoader;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class SemanticHarmonySocialNetworkImpl implements SemanticHarmonySocialNetwork {

	private static final Logger logger = Logger.getAnonymousLogger();

	public static final Double AFFINITY_TRESHOLD = 0.60;
	final private Peer peer;
	final private PeerDHT _dht;
	final private int DEFAULT_MASTER_PORT=4000;
	final private MessageListener listener;
	private String profileKey;
	private String nickname;
	private List<Integer> answers;

	private static final String RELATIONSHIPS = "RELATIONSHIPS_KEY";
	private static final String QUESTIONS = "QUESTIONS";



	public SemanticHarmonySocialNetworkImpl(int id, String master_peer) throws IOException {
			listener= new MessageListenerImpl(id);
		 	peer= new PeerBuilder(Number160.createHash(id)).ports(DEFAULT_MASTER_PORT+id).start();
			_dht = new PeerBuilderDHT(peer).start();
			profileKey=null;
			nickname=null;

			FutureBootstrap fb = _dht.peer().bootstrap().inetAddress(InetAddress.getByName(master_peer)).ports(DEFAULT_MASTER_PORT).start();
			fb.awaitUninterruptibly();
			if(fb.isSuccess()) {
				FutureDiscover fd = _dht.peer().discover().peerAddress(fb.bootstrapTo().iterator().next()).start();
				fd.awaitUninterruptibly();
			}

			_dht.peer().objectDataReply(new ObjectDataReply() {

				public Object reply(PeerAddress sender, Object request) throws Exception {
					return listener.parseMessage(request);
				}
			});
	}

	public String getNickname(){
		return nickname;
	}
	public List<String> getUserProfileQuestions() {
		List<String> questions;
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(QUESTIONS)).start();
			futureGet.awaitUninterruptibly();
			if ((futureGet.isSuccess()) && (!futureGet.isEmpty())) {
				questions = (List<String>) futureGet.dataMap().values().iterator().next().object();
			} else {
				questions = QuestionsLoader.getQuestions();
				_dht.put(Number160.createHash(QUESTIONS)).data(new Data(questions)).start().awaitUninterruptibly();
			}

		}
		catch (Exception e) {
			questions = QuestionsLoader.getQuestions();
			e.printStackTrace();
		}
		return questions;
	}

	public String getProfileKey(){
		return profileKey;
	}

	public List<Integer> getAnswers() {
		return answers;
	}

	public String createAuserProfileKey(List<Integer> _answer) {
		this.answers = _answer;
		String answer = getString(_answer);
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(RELATIONSHIPS)).start();
			futureGet.awaitUninterruptibly();
			HashSet<String> relationships;
			if ((futureGet.isSuccess()) && (!futureGet.isEmpty())) {
				relationships = (HashSet<String>) futureGet.dataMap().values().iterator().next().object();
				for(String relation : relationships) {
					Double affinity = calculateAffinity(answer, relation);
					if(affinity >= AFFINITY_TRESHOLD) return relation;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}

	public static Double calculateAffinity(List<Integer> answers, String relation){
		return calculateAffinity(getString(answers), relation);
	}

	private static Double calculateAffinity(String answer, String relation) {
		int shared = 0;
		for(int i=0; i < answer.length(); i++)
			if(answer.charAt(i) == relation.charAt(i)) shared++;
		double affinity = shared / (double) answer.length();
		logger.info("\n"+answer+"\n"+relation+ "\n+"+answer.replaceAll(".","-")+" => "+affinity);
		return affinity;
	}

	private static String getString(List<Integer> answer) {
		String s = "";
		for(int i=0; i < answer.size(); i++) s += answer.get(i);
		return s;
	}

	private void createRelationship(String relationship){
        FutureGet futureGet = _dht.get(Number160.createHash(RELATIONSHIPS)).start();
        futureGet.awaitUninterruptibly();
        HashSet<String> relationships;
        try {
            if (futureGet.isSuccess()) {
                if (futureGet.isEmpty())
                    relationships = new HashSet<String>();
                else
                    relationships = (HashSet<String>) futureGet.dataMap().values().iterator().next().object();
                relationships.add(relationship);
                _dht.put(Number160.createHash(RELATIONSHIPS)).data(new Data(relationships)).start().awaitUninterruptibly();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public boolean join(String _profile_key, String _nick_name) {
		User myUser=new User();
		myUser.setAddress(_dht.peer().peerAddress());
		myUser.setNickname(_nick_name);
		profileKey=_profile_key;
		nickname=_nick_name;
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(_profile_key)).start();
			futureGet.awaitUninterruptibly();
			HashSet<User> users;
			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) {
                    createRelationship(_profile_key);
                    users = new HashSet<User>();
                } else {
					users = (HashSet<User>) futureGet.dataMap().values().iterator().next().object();
					for(User user:users) {
						FutureDirect futureDirect = _dht.peer().sendDirect(user.getAddress()).object(_nick_name+" è un nuovo amico").start();
						futureDirect.awaitUninterruptibly();
					}
				}
				users.add(myUser);
				_dht.put(Number160.createHash(_profile_key)).data(new Data(users)).start().awaitUninterruptibly();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}


	private List<User> getFriendsAsUser() {
        List<User> friends = new ArrayList<>();
        try {
            FutureGet futureGet = _dht.get(Number160.createHash(profileKey)).start();
            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess() && !futureGet.isEmpty()) {
                Set<User> users = (HashSet<User>) futureGet.dataMap().values().iterator().next().object();
                for(User friend : users)
                    if (!friend.getNickname().equals(nickname)) friends.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friends;
    }


	public List<String> getFriends() {
	    List<User> friends = getFriendsAsUser();
	    List<String> toReturn = new ArrayList<>();
	    for(User friend: friends) toReturn.add(friend.getNickname());
		return toReturn;
	}
		

	class MessageListenerImpl implements MessageListener{
		int peerid;
		public MessageListenerImpl(int peerid)
		{
			this.peerid=peerid;
		}

		public Object parseMessage(Object obj) {
			System.out.println(peerid+"] "+obj);
			return "success";
		}
}


    public static SemanticHarmonySocialNetworkImpl join(Integer id, String nickname, String master_peer) throws IOException {
        SemanticHarmonySocialNetworkImpl peer = new SemanticHarmonySocialNetworkImpl(id, master_peer);
        System.out.println("Hi "+nickname+"! Welcome in the Semantic Harmony Social Network!\n" +
                "Please ask this questions!\n" );
	    List<Integer> answers = askQuestionsFake(peer.getUserProfileQuestions());
	    String profileKey = peer.createAuserProfileKey(answers);
	    System.out.println(nickname+"'s profile key: "+profileKey);
	    if(!peer.join(profileKey, nickname)) {
	        System.out.println("Something goes wrong during join!!");
	        return null;
        }
        System.out.println(nickname + " logged.\n\n");
	    return peer;
    }
    private static List<Integer> askQuestions(List<String> questions) {
        List<Integer> answers = new ArrayList<>();
        Scanner reader = new Scanner(System.in);
        for(int i=0; i < questions.size(); i++){
            System.out.println(i+")1\t"+questions.get(i));
            answers.add(reader.nextInt());
        }
        reader.close();
        return answers;
    }


    private static List<Integer> askQuestionsFake(List<String> questions) {
    	Random random = new Random();
        List<Integer> answers = new ArrayList<>();
        for(int i=0; i<questions.size(); i++) {
			answers.add(random.nextInt(2));
		}
        return answers;
	}

    public static void main(String[] args) throws IOException {
		String master_peer = "127.0.0.1";
	    SemanticHarmonySocialNetworkImpl peer0 = join(0, "0", master_peer);
		int numPeers = 50;
		List<SemanticHarmonySocialNetworkImpl> peers = new ArrayList<>();
		for(int i=1; i < numPeers; i++) peers.add(join(i, i+"", master_peer));
		System.exit(0);
 }



}
	
