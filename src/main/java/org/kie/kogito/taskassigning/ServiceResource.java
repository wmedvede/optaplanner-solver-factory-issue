package org.kie.kogito.taskassigning;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.kie.kogito.taskassigning.core.model.TaskAssigningSolution;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Startup
@Path("/service")
public class ServiceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    SolverFactory<TaskAssigningSolution> solverFactory;

    @Inject
    ManagedExecutor managedExecutor;

    /**
     * Try to start the solver at the very beginning to see the error, But if we would have executed the method
     * executeStartSolver as part of a REST call we get the same error.
     */
    @PostConstruct
    void start() {
        startSolver();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String executeStartSolver() {
        managedExecutor.runAsync(this::startSolver);
        return "Solver start programmed!";
    }

    private void startSolver() {
        TaskAssigningSolution solution = Util.createSolution();
        LOGGER.debug("About to start the Solver");

        managedExecutor.execute(() -> {
            try {
                Solver<TaskAssigningSolution> solver = solverFactory.buildSolver();
                solver.solve(solution);
                LOGGER.debug("Solver started with no problem.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}