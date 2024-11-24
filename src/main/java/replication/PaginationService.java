package replication;

import com.google.gson.Gson;
import entity.*;
import services.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationService {
    private static final int BATCH_SIZE = 10;
    private final ReplicateDataToServer2 replicateDataToServer2;
    private final Gson gson;

    public PaginationService() {
        this.replicateDataToServer2 = new ReplicateDataToServer2();
        this.gson = new Gson();
    }

    public <T> void synchronizeData(PaginatedDataFetcher<T> fetcher, Timestamp lastSync, String entityKey) {

        int totalRecords = fetcher.getTotalRecordCount(lastSync);

        if (totalRecords == 0) return;

        int totalBatches = (int) Math.ceil((double) totalRecords / BATCH_SIZE);

        for (int batch = 0; batch < totalBatches; batch++) {
            int offset = batch * BATCH_SIZE;

            List<T> batchData = fetcher.fetchPaginatedData(lastSync, offset, BATCH_SIZE);

            Map<String, Object> batchPayload = new HashMap<>();
            batchPayload.put(entityKey, batchData);

            String jsonBatch = gson.toJson(batchPayload);
            replicateDataToServer2.sendDataToServer2(jsonBatch);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void synchronizeAllEntities(Timestamp lastSync) {
        // Departments
        synchronizeData(
                new PaginatedDataFetcher<Department>() {
                    private final DepartmentService service = new DepartmentService();

                    @Override
                    public List<Department> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedNewDepartments(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getNewDepartmentsCount(lastSync);
                    }
                },
                lastSync,
                "newDepartments"
        );

        // Deleted Departments
        synchronizeData(
                new PaginatedDataFetcher<Integer>() {
                    private final DepartmentService service = new DepartmentService();

                    @Override
                    public List<Integer> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedDeletedDepartmentIds(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getDeletedDepartmentIdsCount(lastSync);
                    }
                },
                lastSync,
                "deletedDepartments"
        );

        // Employees
        synchronizeData(
                new PaginatedDataFetcher<Employee>() {
                    private final EmployeeService service = new EmployeeService();

                    @Override
                    public List<Employee> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedNewEmployees(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getNewEmployeesCount(lastSync);
                    }
                },
                lastSync,
                "employeesData"
        );

        // Deleted Employees
        synchronizeData(
                new PaginatedDataFetcher<Integer>() {
                    private final EmployeeService service = new EmployeeService();

                    @Override
                    public List<Integer> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedDeletedEmployeeIds(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getDeletedEmployeeIdsCount(lastSync);
                    }
                },
                lastSync,
                "deletedEmployees"
        );

        // Employee Types
        synchronizeData(
                new PaginatedDataFetcher<EmployeeType>() {
                    private final EmployeeTypeService service = new EmployeeTypeService();

                    @Override
                    public List<EmployeeType> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedNewEmployeeTypes(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getNewEmployeeTypesCount(lastSync);
                    }
                },
                lastSync,
                "newEmployeeTypes"
        );

        // Deleted Employee Types
        synchronizeData(
                new PaginatedDataFetcher<Integer>() {
                    private final EmployeeTypeService service = new EmployeeTypeService();

                    @Override
                    public List<Integer> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedDeletedEmployeeTypeIds(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getDeletedEmployeeTypeIdsCount(lastSync);
                    }
                },
                lastSync,
                "deletedEmployeeTypes"
        );

        // Users
        synchronizeData(
                new PaginatedDataFetcher<User>() {
                    private final UserService service = new UserService();

                    @Override
                    public List<User> fetchPaginatedData(Timestamp lastSync, int offset, int limit) {
                        return service.getPaginatedUpdatedUsers(lastSync, offset, limit);
                    }

                    @Override
                    public int getTotalRecordCount(Timestamp lastSync) {
                        return service.getUpdatedUsersCount(lastSync);
                    }
                },
                lastSync,
                "users"
        );
    }
}