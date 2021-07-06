package org.kie.kogito.taskassigning;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.Test;
import org.kie.kogito.taskassigning.core.model.TaskAssigningSolution;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test works fine.
 */
@QuarkusTest
class UseSolverFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UseSolverFactoryTest.class);

    @Inject
    SolverFactory<TaskAssigningSolution> solverFactory;

    @Inject
    ManagedExecutor managedExecutor;

    @Test
    void initSolver() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TaskAssigningSolution solution = Util.createSolution();

        LOGGER.debug("Create the Solver");
        Solver<TaskAssigningSolution> solver = solverFactory.buildSolver();

        LOGGER.debug("Add Solver listener");
        solver.addEventListener(event -> {
            LOGGER.debug("New solution arrived!" + event.getNewBestSolution().getScore());
            countDownLatch.countDown();
        });

        LOGGER.debug("Start the Solver");
        // using managed executor to be as close as possible to real scenario, CompletableFuture.runAsync gives same results.
        managedExecutor.runAsync(() -> {
            solver.solve(solution);
        });

        LOGGER.debug("Waiting for the first solution to arrive");
        countDownLatch.await();
        solver.terminateEarly();
    }
}
