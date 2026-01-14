# reactive-kaprecar

The purpose of this project is to use the reactive paradigm.

This project has two ways of working: 
 - synchronous one
 - asynchronous one 

Everything is executed in a full reactive way.

## Synchronous behaviour

This execution mode exposes only one `GET` API `/calculation/{number}` where you need to insert a 4 digits number with at least 2 different digits.
The output of the api is a JSON with the convergence to the Kaprekar’s constant https://en.wikipedia.org/wiki/Kaprekar%27s_routine .

The internal logic of the project is the following when the API is invoked:
 - [x] Validate input, the number needs to fulfill the constraints 
 - [x] If the computation is already been executed in the past return the already calculated result
 - [x] If the computation isn't already been executed, execute the calculation synchronously 



## Asynchronous behaviour

The asyncronous behavior is triggered from a idempotent `POST` to the same API. 

- [x] execute a POST on `/calculation/{number}` in order to trigger the calculation
- [x] send a message to a Kafka topic 

The backend of the system should:
- [x] Listen to the Kafka topic for a new computation to be executed
- [x] Do a lookup in the database to check if the computation is already executed
- [x] If is a new execution execute the computation and Save the result on the database





This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/reactive-kaprecar-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Mutiny ([guide](https://quarkus.io/guides/mutiny-primer)): Write reactive applications with the modern Reactive Programming library Mutiny
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Messaging - Kafka Connector ([guide](https://quarkus.io/guides/kafka-getting-started)): Connect to Kafka with Reactive Messaging
- Reactive PostgreSQL client ([guide](https://quarkus.io/guides/reactive-sql-clients)): Connect to the PostgreSQL database using the reactive pattern
- SmallRye Context Propagation ([guide](https://quarkus.io/guides/context-propagation)): Propagate contexts between managed threads in reactive applications

## Provided Code

### Messaging codestart

Use Quarkus Messaging

[Related Apache Kafka guide section...](https://quarkus.io/guides/kafka-reactive-getting-started)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
