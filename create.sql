/* 建立TinyLog数据库 */
CREATE SCHEMA `tinylog` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

CREATE TABLE `tinylog`.`user` (
  `username` VARCHAR(32) NOT NULL,
  `realname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `location` VARCHAR(60) NULL,
  `password` VARCHAR(45) NOT NULL,
  `authority` SMALLINT NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC)
);

CREATE TABLE `tinylog`.`articles` (
  `article_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `summary` VARCHAR(256) NOT NULL,
  `content` VARCHAR(5000) NOT NULL,
  `tags` VARCHAR(45) NULL,
  `post_time` DATETIME NOT NULL,
  PRIMARY KEY (`article_id`),
  UNIQUE INDEX `article_id_UNIQUE` (`article_id` ASC),
  INDEX `fk_articles_1_idx` (`username` ASC),
  CONSTRAINT `fk_articles_1`
    FOREIGN KEY (`username`)
    REFERENCES `tinylog`.`user` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE `tinylog`.`comment_list` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `article_id` INT NULL,
  `content` VARCHAR(256) NOT NULL,
  `usertype` INT NOT NULL,
  `nickname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(50) NULL,
  `website` VARCHAR(100) NULL,
  `post_time` DATETIME NOT NULL,
  PRIMARY KEY (`comment_id`),
  UNIQUE INDEX `comment_id_UNIQUE` (`comment_id` ASC),
  INDEX `fk_comment_list_1_idx` (`article_id` ASC),
  CONSTRAINT `fk_comment_list_1`
    FOREIGN KEY (`article_id`)
    REFERENCES `tinylog`.`articles` (`article_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE `tinylog`.`sys_info` (
  `property` VARCHAR(45) NOT NULL,
  `value` VARCHAR(256) NULL,
  PRIMARY KEY (`property`),
  UNIQUE INDEX `property_UNIQUE` (`property` ASC)
);

CREATE TABLE `tinylog`.`msg_list` (
  `msg_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NOT NULL,
  `msg_type` VARCHAR(45) NOT NULL,
  `content` VARCHAR(512) NOT NULL,
  `target` VARCHAR(45) NULL,
  `read` INT NULL,
  `post_time` DATETIME NOT NULL,
  PRIMARY KEY (`msg_id`),
  UNIQUE INDEX `msg_id_UNIQUE` (`msg_id` ASC),
  INDEX `fk_msg_list_1_idx` (`username` ASC),
  CONSTRAINT `fk_msg_list_1`
    FOREIGN KEY (`username`)
    REFERENCES `tinylog`.`user` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

/* 文章视图 */
CREATE VIEW `tinylog`.`article_detail` AS
SELECT `realname`, COUNT(`comment_id`) `replies`, articles.* 
FROM `tinylog`.`articles` 
LEFT JOIN `tinylog`.`comment_list` ON(`articles`.`article_id` = `comment_list`.`article_id`) 
LEFT JOIN `tinylog`.`user` ON(`user`.`username` = `articles`.`username`) GROUP BY `articles`.`article_id`;

/* 用户认证视图 */
CREATE VIEW `tinylog`.`user_auth` AS
SELECT username, password, authority FROM `tinylog`.`user`;
/* 用户信息视图 */
CREATE VIEW `tinylog`.`user_info` AS
SELECT username, realname, email, location FROM `tinylog`.`user`;
/* 建立原初管理员账户 */
INSERT INTO `tinylog`.`user`(username, realname, email, location, password, authority) 
VALUES("root", "Jakes Lee", "jakeslee66@gmail.com", "China",  "7c4a8d09ca3762af61e59520943dc26494f8941b", 2);
/* 初始化系统信息 */
INSERT INTO `tinylog`.`sys_info` (`property`, `value`) VALUES ('homeurl', 'http://localhost:8080');
INSERT INTO `tinylog`.`sys_info` (`property`, `value`) VALUES ('motto', '站在阳光下，享受我单薄的青春');
INSERT INTO `tinylog`.`sys_info` (`property`, `value`) VALUES ('name', '翔腾苍穹');
INSERT INTO `tinylog`.`sys_info` (`property`, `value`) VALUES ('notice', '这里是酷酷的公告栏，目前功能还很简单，其他功能正在努力实现，敬请期待！');

/* 文章删除触发器 */
CREATE TRIGGER `tinylog`.`tgr_article_delete`
AFTER DELETE
ON `tinylog`.`articles`
FOR EACH ROW
	DELETE FROM `comment_list` WHERE article_id = old.article_id;

/* -------------------- */