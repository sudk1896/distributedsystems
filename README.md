# Distributed Systems Class University of Salerno

This is the practical source for the Distributed System class of Computer Science @ University of Salerno of Professor Alberto Negro.

#### Referents
- Alberto Negro (foundamentals and algorithms).
- Gennaro Cordasco and  Carmine Spagnuolo (P2P).

## Projects Design Requirements

Projects must be developed using Java languages (also, using [Apache Maven](https://maven.apache.org/) as software project management) and [TomP2P](https://tomp2p.net/) framework/library.
TomP2P is a DHT with additional features, such as storing multiple values for a key. Each peer has a table (either disk-based or memory-based) to store its values. A single value can be queried / updated with a secondary key. The underlying communication framework uses Java NIO to handle many concurrent connections.

## Homeworks Prerequisites

- Concurrent and Object-oriented programming fundamental (Threads and Observer pattern are required).
- Distributed System foundamental (Distributed Hash Tables, DHT is required).
- Java 7 or greater.
- Apache Maven.
- Eclipse (optional).


## Homeworks

For each homework is presented a Java interface API that must be implemented by a solution (the API are available in the folder challenges).

Five projects to be developed on a P2P Network:

- *Publish/Subscribe Protocol* ([example project](https://github.com/spagnuolocarmine/p2ppublishsubscribe))
- *Anonymous Chat*
- *Git Protocol*
- *Auction Mechanism*
- *Sudoku Game*
- *Semantich Harmony Social Network*


## Homework Submission

Solutions must be a Java Maven project, that implements the problem API, and includes at minimum one test case written using JUnit (unit test case for Java).
Each group leader should require by mail to cspagnuolo@unisa.it its access on GitHub and can place in the folder solutions/2017_18/group_leader_surname the homework solution. Each project must includes a README file, written in markdown, in the root of the project. The README describes the faced project, briefly the solution, the projects members, and eventually test cases. 

#### Dependences 

Usign Maven you can add the dependencies to TomP2P in the pom.xml file. 

```
<repositories>
    <repository>
        <id>tomp2p.net</id>
         <url>http://tomp2p.net/dev/mvn/</url>
     </repository>
</repositories>
<dependencies>
   <dependency>
     <groupId>net.tomp2p</groupId>
     <artifactId>tomp2p-all</artifactId>
      <version>5.0-Beta8</version>
   </dependency>
</dependencies>
```

---------------------------------------------------------------------------------------------------
## Homeworks Descriptions

### Anonymous Chat

<img align="right" src=https://s3.amazonaws.com/lowres.cartoonstock.com/telecommunications-chatting-chatroom-chatting_over_the_fence-neighbour-gossip-gri0032_low.jpg width="300"/>

Design and develop an anonymous chat API based on P2P Network. Each peer can send messages on public chat room in anonymous way. The system allows the users to create new room, join in a room, leave a room and send a message on a room. As described in the [AnonymousChat Java API](https://github.com/spagnuolocarmine/distributedsystems/blob/master/challenges/AnonymousChat.java).

&nbsp;

&nbsp;

&nbsp;

&nbsp;

&nbsp;

&nbsp;

&nbsp;

### Git Protocol

<img align="left" src= https://i2.wp.com/makingbones.files.wordpress.com/2013/02/picture12.jpg width="300"/>

Design and develop the Git protocol distributed versioning control on a P2P network. Each peer can manage its projects (a set of files) using the Git protocol (a minimal version of it). The system allows the users to create a new repository in a specific foleder, adds new files to be traked by the system, apply the changin on the local repository (commit function), push the changin in the network and pull the changing from the network. The git protocol has a lot specific behaviors to manage the conflicts, in this version is only required that if there are some conflicts the systems can download the remote copy and the merge is manually done. As described in the [GitProtocol Java API](https://github.com/spagnuolocarmine/distributedsystems/blob/master/challenges/GitProtocol.java).

&nbsp;

&nbsp;

&nbsp;

&nbsp;


### Auction Mechanism

<img align="left" src =https://s3.amazonaws.com/lowres.cartoonstock.com/law-order-online_auction-blackmarket-black_market-website-stolen_good-bmun149_low.jpg width="300"/>

Design and develop an auction mechanism based on P2P Network. Each peer can sell and buy goods using a Second-Price Auctions (EBay). 
second-price auction is a non-truthful auction mechanism for multiple items. Each bidder places a bid. The highest bidder gets the first slot, the second-highest, the second slot and so on, but the highest 
bidder pays the price bid by the second-highest bidder, the second-highest pays the price bid by the third-highest, and so on. The systems allows the users to create new auction (with an ending time, a reserved selling price and a description), checks the status of an auction, and eventually place new bid for an auction.

&nbsp;

&nbsp;

&nbsp;


### Sudoku Game

<img align="right" src =https://qph.ec.quoracdn.net/main-qimg-6ffebac2a0f3b17fd558c6caa21d87b9  width="300"/>



### Semantich Harmony Social Network

<img align="left" src= https://s3.amazonaws.com/lowres.cartoonstock.com/media-social_media-interests-compatibility-dates-social_networks-jsh120327_low.jpg  width="300"/>


