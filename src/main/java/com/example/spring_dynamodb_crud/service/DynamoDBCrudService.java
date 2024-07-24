package com.example.spring_dynamodb_crud.service;

import com.example.spring_dynamodb_crud.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoDBCrudService {

    private final String tableName; 
    private final DynamoDbClient dbClient;

    public DynamoDBCrudService(@Value("${aws.dynamodb.table-name}") String tableName) {
        this.tableName = tableName;
        this.dbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    public String createUser(User user) {
        HashMap<String, AttributeValue> items = new HashMap<>();
        items.put("userId", AttributeValue.builder().s(user.getUserId()).build());
        items.put("name", AttributeValue.builder().s(user.getName()).build());
        items.put("age", AttributeValue.builder().n(String.valueOf(user.getAge())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(items)
                .build();
        try {
            dbClient.putItem(request);
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
        return "User created successfully";
    }

    public User readUser(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        try {
            Map<String, AttributeValue> item = dbClient.getItem(request).item();
            if(item != null) {
                User user = new User();
                user.setUserId(item.get("userId").s());
                user.setName(item.get("name").s());
                user.setAge(Integer.parseInt(item.get("age").n()));
                return user;
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    public String updateUser(User user) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(user.getUserId()).build());
        Map<String, AttributeValueUpdate> updateValues = new HashMap<>();
        updateValues.put("name", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(user.getName()).build())
                .action(AttributeAction.PUT)
                .build());
        updateValues.put("age", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(user.getAge())).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updateValues)
                .build();
        try {
            dbClient.updateItem(request);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
        return "User updated successfully";
    }

    public String deleteUser(String userId) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        try {
            dbClient.deleteItem(request);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
        return "User deleted successfully";
    }

}
