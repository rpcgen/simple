Simple test of code with Spring Boot
====================================

Domain
------

A comment system with basic CRUD operations:

- Create new comment
  - Removing any spam text / offensive language
- Up vote a comment
- Link a comment as a response
- Retrieve all comments
- Retrieve all comments ordered by a rank system
- Delete a comment

API
---

PUT /comment
POST /comment/{id}/up-vote
POST /comment/{id}/in-response-to/{parent}
GET /comment
GET /comment/ranked
DELETE /comment/{id}

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

TECHNICAL DEBT
--------------

- Lack of RPM packaging
- Lack of Docker integration
- Lack of logs and any element to provide operative information

RISKS
-----

- Malicious users can publish inappropriate content
  - Mitigated : Spam filters
  - May require human review (high costs)

- Externalize logic (com.simple.rank script) could make system unstable
  - Mitigated : Define a business process to publish a new script

- Technical debt put in risk operations (logs, JMX, stats)
  - Involve infrastructure to provide non functional requirements

KNOWN BUGS
----------

The functionality to link two comments allow the user to create circular references