# Semantic Harmony Social Network #

### What is it  ###
Semantic Harmony Social Network is a social network based on the users interests, developed in Java that exploits a P2P Network using TomP2P.

### How it works  ###
The system connects users with similar interests.

When a user connects to the network, he takes the list of active social rooms from the distributed hash table.
Each social room is characterized by particular user interests.

By submitting a series of questions to the user, he is placed in the first compatible room and at any time can pick up the list of his friends and get in touch.

### How to install ###

```
git clone
cd SemanticHarmonySocialNetwork
mvn test install
```