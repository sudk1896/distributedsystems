package it.unisa.sisd.bean;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -1316622755169272306L;

	private PeerAddress address;
	private String nickname;

	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public PeerAddress getAddress() {
		return address;
	}
	public void setAddress(PeerAddress address) {
		this.address = address;
	}
	

}
