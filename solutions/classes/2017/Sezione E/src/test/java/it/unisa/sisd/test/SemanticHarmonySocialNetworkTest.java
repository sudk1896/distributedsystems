package it.unisa.sisd.test;


import it.unisa.sisd.socialnetwork.SemanticHarmonySocialNetwork;
import it.unisa.sisd.socialnetwork.SemanticHarmonySocialNetworkImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SemanticHarmonySocialNetworkTest {

    private static final String MASTER_PEER = "127.0.0.1";
    int numPeers = 50;

    @Test
    public void testGetQuestions() throws IOException {
        SemanticHarmonySocialNetwork peer0 = SemanticHarmonySocialNetworkImpl.join(1000, "100", MASTER_PEER);
        assertTrue(peer0.getUserProfileQuestions().size() > 0);
    }

    @Test
    public void testGetCreateAUserProfileKey() throws IOException {
        SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(10000, MASTER_PEER);
        List<Integer> answers0 = Arrays.asList(new Integer[] {0, 0, 0, 0, 0, 0});
        String profileKey0 = peer0.createAuserProfileKey(answers0);
        peer0.join(profileKey0, "A");
        SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(10001, MASTER_PEER);
        List<Integer> answers1 = Arrays.asList(new Integer[] {1, 0, 0, 0, 0, 0});
        String profileKey1 = peer0.createAuserProfileKey(answers1);
        peer1.join(profileKey1, "B");
        SemanticHarmonySocialNetworkImpl peer2 =  new SemanticHarmonySocialNetworkImpl(10002, MASTER_PEER);
        List<Integer> answers2 = Arrays.asList(new Integer[] {1, 1, 1, 1, 1, 1});
        String profileKey2 = peer0.createAuserProfileKey(answers2);
        peer2.join(profileKey2, "C");
        assertEquals(peer0.getProfileKey(), peer1.getProfileKey());
        assertNotEquals(peer0.getProfileKey(), peer2.getProfileKey());
    }


    @Test
    public void testJoin() throws IOException {
        SemanticHarmonySocialNetwork peer0 = SemanticHarmonySocialNetworkImpl.join(0, "0", MASTER_PEER);
        List<SemanticHarmonySocialNetwork> peers = new ArrayList<>();
        for(int i=1; i < numPeers; i++) {
            SemanticHarmonySocialNetworkImpl newPeer = SemanticHarmonySocialNetworkImpl.join(i, i + "", MASTER_PEER);
            assert(newPeer != null);
            assert(SemanticHarmonySocialNetworkImpl.calculateAffinity(newPeer.getAnswers(), newPeer.getProfileKey()) >= SemanticHarmonySocialNetworkImpl.AFFINITY_TRESHOLD);
            peers.add(newPeer);
        }
    }

    @Test
    public void testGetFriends() throws IOException {
        SemanticHarmonySocialNetworkImpl peer0 = new SemanticHarmonySocialNetworkImpl(20000, MASTER_PEER);
        List<Integer> answers0 = Arrays.asList(new Integer[] {0, 0, 0, 0, 0, 0});
        String profileKey0 = peer0.createAuserProfileKey(answers0);
        peer0.join(profileKey0, "Amico di b");
        SemanticHarmonySocialNetworkImpl peer1 = new SemanticHarmonySocialNetworkImpl(20001, MASTER_PEER);
        List<Integer> answers1 = Arrays.asList(new Integer[] {1, 0, 0, 0, 0, 0});
        String profileKey1 = peer0.createAuserProfileKey(answers1);
        peer1.join(profileKey1, "Amico di a");
        SemanticHarmonySocialNetworkImpl peer2 =  new SemanticHarmonySocialNetworkImpl(30002, MASTER_PEER);
        List<Integer> answers2 = Arrays.asList(new Integer[] {2, 2, 2, 2, 2, 2});
        String profileKey2 = peer0.createAuserProfileKey(answers2);
        peer2.join(profileKey2, "Non mi piace nessuno");
        assertTrue(peer0.getFriends().contains(peer1.getNickname()));
        assertTrue(peer1.getFriends().contains(peer0.getNickname()));
        assertFalse(peer0.getFriends().contains(peer2));
        assertFalse(peer1.getFriends().contains(peer2));
        assertFalse(peer2.getFriends().contains(peer0));
        assertFalse(peer2.getFriends().contains(peer1));
    }
}
