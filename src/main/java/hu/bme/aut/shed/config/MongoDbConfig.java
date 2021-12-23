package hu.bme.aut.shed.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoDbConfig extends AbstractMongoClientConfiguration {
    @Override
    public MongoClient mongoClient() {
        System.setProperty("jasypt.encryptor.password", "sheddb");

        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:asdasd@cluster0.m7rj9.mongodb.net/shed");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return "shed";
    }
}