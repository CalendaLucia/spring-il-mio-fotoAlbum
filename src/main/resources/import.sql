INSERT INTO `categories`(`name`) VALUES ('funny');
INSERT INTO `categories`(`name`) VALUES ('love');

INSERT INTO `roles`(`id`,`name`) VALUES (1, 'ADMIN');
INSERT INTO `roles`(`id`,`name`) VALUES (2, 'USER');

INSERT INTO `users`(`id`, `email`, `first_name`, `last_name`, `password`) VALUES (1, 'mariorossi@gmail.com','Mario','Rossi','{noop}mario');
INSERT INTO `users`(`id`, `email`, `first_name`, `last_name`, `password`) VALUES (2, 'laurabianchi@gmail.com','Laura','Bianchi','{noop}laura');

INSERT INTO `users_roles`(`user_id`, `roles_id`) VALUES ('1','1')
INSERT INTO `users_roles`(`user_id`, `roles_id`) VALUES ('2','2')