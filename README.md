Simple test of code with Spring Boot
====================================

Domain
------

A comment system

API
---

**PUT /comment**
Create new comment removing any spam text / offensive language

**POST /comment/{id}/up-vote**
A positive vote to a comment

**POST /comment/{id}/in-response-to/{parent}**
Link a two comments as original and response

**GET /comment**
Retrieve all comments

**GET /comment/ranked**
Retrieve all comments ordered by a rank system

**DELETE /comment/{id}**
Delete a comment

DEVELOPMENT DETAILS
-------------------

- Spring boot
  - An example of AOP programming (Status204IfEmptyCollectionAspect)

- Tests
  - Nice unit testing isolating the element tested
  - Nice unit testing verifying the interaction
  - Integration tests to verify the final contract (still more tests needed to check the business)

- Java8
  - Using stream to process spam texts
  - Using LocalDate on entities

- Groovy
  - Groovy script to externalize the logic of comment ranking

RISKS
-----

- Malicious users can publish inappropriate content
  - Mitigated : Spam filters
  - May require human review (high costs)

- Externalize logic (com.simple.rank script) could make system unstable
  - Mitigated : Define a business process to publish a new script

- Technical debt put in risk operations (logs, JMX, stats)
  - Involve infrastructure to provide non functional requirements

ENHANCEMENTS
------------

https://github.com/rpcgen/simple/issues?q=is:issue+is:open+label:enhancement

KNOWN BUGS
----------

https://github.com/rpcgen/simple/issues?q=is:issue+is:open+label:bug