INSERT INTO users (id, email, password, role) VALUES (2,'test@gmail.com', 'test', 'ADMIN');
INSERT INTO users (id, email, password, role) VALUES (3,'user2', 'pass2','USER');
INSERT INTO users (id, email, password, role) VALUES (4,'user3', 'pass3', 'USER');
INSERT INTO users (id, email, password, role) VALUES (5,'user4', 'pass4','ADMIN');
INSERT INTO users (id, email, password, role) VALUES (6,'user5', 'pass5','ADMIN');
INSERT INTO users (id, email, password, role) VALUES (7,'userAsign@gmail.com', 'pass5','ADMIN');


-- Inserting tasks
INSERT INTO tasks (id, title, description, status, priority, author_id, assignee_id, created_at, updated_at) VALUES
(2, 'Task 2', 'Description for Task 2', 'PENDING', 'HIGH', 2, 3, NOW(), NOW()),
(3, 'Task 3', 'Description for Task 3', 'IN_PROGRESS', 'MEDIUM', 2, 4, NOW(), NOW()),
(4, 'Task 4', 'Description for Task 4', 'COMPLETED', 'LOW', 2, 3, NOW(), NOW());

-- Inserting comments
INSERT INTO comments (id, content, user_id, task_id, created_at) VALUES
(2, 'This is a comment for Task 3', 2, 3, NOW()),
(3, 'Another comment for Task 3', 4, 3, NOW()),
(4, 'Comment for Task 2', 2, 2, NOW()),
(5, 'Feedback on Task 3', 3, 3, NOW());
