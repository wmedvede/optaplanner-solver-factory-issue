To reproduce the issue execute do:

1) mvn clean install
2) java -jar target/optaplanner-solver-factory-issue-1.0.0-SNAPSHOT-runner.jar

On the other hand the following test works fine:

mvn test -Dtest=org.kie.kogito.taskassigning.UseSolverFactoryTest
