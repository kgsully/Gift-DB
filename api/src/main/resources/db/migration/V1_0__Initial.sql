CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(256) NOT NULL UNIQUE,
    hashedPassword VARCHAR BINARY(255) NOT NULL,
    accountAdmin BOOLEAN NOT NULL,
    linkedTo INTEGER,
    active BOOLEAN NOT NULL,
    name VARCHAR (50) NOT NULL,
    createdAt DATE NOT NULL,
    updatedAt DATE NOT NULL
    );

INSERT INTO users (username, email, hashedPassword, accountAdmin, linkedTo, active, name, createdAt, updatedAt)
  VALUES ('admin',
          'admin@test.com',
          '$2a$10$mQTJAAy1Kw2kgh0UzCB3Yejy8g8gfYrA51K74GR.qAuH3n3aZji9m',
          true,
          NULL,
          1,
          "Admin Account",
          '2024-12-17T18:17:51Z',
          '2024-12-17T18:17:51Z'
          );

 INSERT INTO users (username, email, hashedPassword, accountAdmin, linkedTo, active, name, createdAt, updatedAt)
   VALUES ('testuser',
           'user@test.com',
           '$2a$10$mQTJAAy1Kw2kgh0UzCB3Yejy8g8gfYrA51K74GR.qAuH3n3aZji9m',
           false,
           1,
           1,
           "User Account",
           '2024-12-17T18:17:51Z',
           '2024-12-17T18:17:51Z'
           );