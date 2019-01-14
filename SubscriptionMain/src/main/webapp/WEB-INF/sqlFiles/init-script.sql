USE `times_subscription`;

CREATE TABLE `user`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) DEFAULT NULL,
  `mobile` varchar(32) NOT NULL,
  `prime_id` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `sso_id` varchar(64) NOT NULL,
  `city` varchar(32) NOT NULL,
  `blocked` tinyint(1) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `INDEX_SSO_ID` (`sso_id`),
  INDEX `INDEX_MOBILE` (`mobile`),
  INDEX `INDEX_PRIME_ID` (`prime_id`),
  INDEX `INDEX_EMAIL` (`email`),
  INDEX `INDEX_USER_BLOCKED` (`blocked`),
  INDEX `INDEX_USER_DELETED` (`deleted`)
);

CREATE TABLE `user_audit`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `event` varchar(128) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) DEFAULT NULL,
  `mobile` varchar(32) NOT NULL,
  `prime_id` varchar(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `sso_id` varchar(64) NOT NULL,
  `city` varchar(32) NOT NULL,
  `blocked` tinyint(1) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `INDEX_UA_USER_ID` (`user_id`),
  INDEX `INDEX_UA_EVENT` (`event`),
  INDEX `INDEX_UA_SSO_ID` (`sso_id`),
  INDEX `INDEX_UA_MOBILE` (`mobile`),
  INDEX `INDEX_UA_EMAIL` (`email`),
  CONSTRAINT `FK_UA_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `subscription_plan`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) NOT NULL,
  `business` varchar(32) NOT NULL,
  `currency` varchar(32) NOT NULL,
  `country` varchar(32) NOT NULL,
  `family` varchar(32) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_name` (`name`),
  INDEX `INDEX_BUSINESS` (`business`),
  INDEX `INDEX_COUNTRY` (`country`),
  INDEX `INDEX_SP_DELETED` (`deleted`)
);

CREATE TABLE `subscription_variant`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscription_plan_id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `plan_type` varchar(32) NOT NULL,
  `recurring` tinyint(1) NOT NULL,
  `price` DECIMAL(5,2) NOT NULL,
  `duration_days` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_name` (`name`),
  INDEX `INDEX_PLAN_TYPE` (`plan_type`),
  INDEX `INDEX_SUBSCRIPTION_PLAN` (`subscription_plan_id`),
  INDEX `INDEX_SV_DELETED` (`deleted`),
  UNIQUE KEY `UK_PLAN_PRICE_DURATION_DAYS` (`subscription_plan_id`, `price`, `duration_days`),
  CONSTRAINT `FK_SUBSCRIPTION` FOREIGN KEY (`subscription_plan_id`) REFERENCES `subscription_plan` (`id`)
);

CREATE TABLE `offer`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subscription_plan_id` int(11) NOT NULL,
  `publisher` varchar(32) NOT NULL,
  `third_party` tinyint(1) NOT NULL,
  `offering` varchar(256) NOT NULL,
  `quantity` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `INDEX_SUBSCRIPTION_PLAN2` (`subscription_plan_id`),
  CONSTRAINT `FK_SUBSCRIPTION2` FOREIGN KEY (`subscription_plan_id`) REFERENCES `subscription_plan` (`id`)
);

CREATE TABLE `user_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket_id` varchar(64) DEFAULT NULL,
  `order_id` varchar(64) DEFAULT NULL,
  `payment_method` varchar(128) DEFAULT NULL,
  `payment_reference` varchar(128) DEFAULT NULL,
  `pg_amount` double DEFAULT 0,
  `tp_reference` varchar(128) DEFAULT NULL,
  `tp_amount` double DEFAULT 0,
  `promo_code` varchar(128) DEFAULT NULL,
  `promo_amount` double DEFAULT 0,
  `order_completed` tinyint(1) NOT NULL,
  `sso_communicated` tinyint(1) NOT NULL,
  `status_published` tinyint(1) NOT NULL,
  `plan_status` varchar(32) NOT NULL,
  `transaction_status` varchar(32) NOT NULL,
  `subscription_variant_id` int(11) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `auto_renewal` tinyint(1) NOT NULL,
  `business` varchar(32) NOT NULL,
  `publisher` varchar(32) NOT NULL,
  `platform` varchar(32) NOT NULL,
  `status` varchar(32) NOT NULL,
  `price` DECIMAL(5,2) NOT NULL,
  `refunded_amount` DECIMAL(5,2) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  INDEX `INDEX_USER` (`user_id`),
  INDEX `INDEX_SUBSCRIPTION_VARIANT` (`subscription_variant_id`),
  INDEX `INDEX_BUSINESS` (`business`),
  INDEX `INDEX_ORDER_COMPLETED` (`order_completed`),
  INDEX `INDEX_SSO_COMMUNICATED` (`sso_communicated`),
  INDEX `INDEX_STATUS_PUBLISHED` (`status_published`),
  INDEX `INDEX_START_DATE` (`start_date`),
  INDEX `INDEX_END_DATE` (`end_date`),
  INDEX `INDEX_STATUS` (`status`),
  INDEX `INDEX_US_DELETED` (`deleted`),
  CONSTRAINT `FK_USER_SUBSCRIPTION_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_SUBSCRIPTION_VARIANT` FOREIGN KEY (`subscription_variant_id`) REFERENCES `subscription_variant` (`id`)
);

CREATE TABLE `user_subscription_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_subscription_id` int(11) NOT NULL,
  `subscription_variant_id` int(11) NOT NULL,
  `subscription_plan_id` int(11) NOT NULL,
  `event` varchar(32) NOT NULL,
  `user_id` int(11) NOT NULL,
  `sso_id` varchar(64) NOT NULL,
  `ticket_id` varchar(64) DEFAULT NULL,
  `order_id` varchar(64) DEFAULT NULL,
  `payment_method` varchar(128) DEFAULT NULL,
  `payment_reference` varchar(128) DEFAULT NULL,
  `pg_amount` double DEFAULT 0,
  `tp_reference` varchar(128) DEFAULT NULL,
  `tp_amount` double DEFAULT 0,
  `promo_code` varchar(128) DEFAULT NULL,
  `promo_amount` double DEFAULT 0,
  `order_completed` tinyint(1) NOT NULL,
  `sso_communicated` tinyint(1) NOT NULL,
  `status_published` tinyint(1) NOT NULL,
  `plan_status` varchar(32) NOT NULL,
  `transaction_status` varchar(32) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `auto_renewal` tinyint(1) NOT NULL,
  `business` varchar(32) NOT NULL,
  `publisher` varchar(32) NOT NULL,
  `platform` varchar(32) NOT NULL,
  `status` varchar(32) NOT NULL,
  `price` DECIMAL(5,2) NOT NULL,
  `refunded_amount` DECIMAL(5,2) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `INDEX_AUDIT_USER_SUBSCRIPTION` (`user_subscription_id`),
  INDEX `INDEX_AUDIT_SUBSCRIPTION_VARIANT` (`subscription_variant_id`),
  INDEX `INDEX_AUDIT_SUBSCRIPTION_PLAN` (`subscription_plan_id`),
  INDEX `INDEX_SSO_ID` (`sso_id`),
  INDEX `INDEX_BUSINESS` (`business`),
  CONSTRAINT `FK_AUDIT_USER_SUBSCRIPTION` FOREIGN KEY (`user_subscription_id`) REFERENCES `user_subscription` (`id`),
  CONSTRAINT `FK_AUDIT_USER_SUBSCRIPTION_VARIANT` FOREIGN KEY (`subscription_variant_id`) REFERENCES `subscription_variant` (`id`),
  CONSTRAINT `FK_AUDIT_SUBSCRIPTION_PLAN` FOREIGN KEY (`subscription_plan_id`) REFERENCES `subscription_plan` (`id`),
  CONSTRAINT `FK_AUDIT_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `subscription_property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_name` varchar(128) NOT NULL,
  `key_value` varchar(256) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `my_idx` (`key_name`),
  UNIQUE KEY `key_name` (`key_name`)
);

CREATE TABLE `external_clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(128) NOT NULL,
  `secret_key` varchar(256) NOT NULL,
  `encryption_key` title NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `my_client` (`client_id`)
);

CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `job_key` varchar(128) NOT NULL,
  `owner` varchar(128) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_NAME` (`name`),
  UNIQUE KEY `INDEX_JOB_KEY` (`job_key`),
  INDEX `INDEX_JOB_DELETED` (`deleted`)
);

CREATE TABLE `job_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `owner` varchar(128) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `records_affected` int(11) NOT NULL,
  `affected_model_details` longtext DEFAULT NULL,
  `response` longtext DEFAULT NULL,
  `exception` longtext DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_AUDIT_JOB` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
);

CREATE TABLE `backend_subscription_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(16) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `code` varchar(16) NOT NULL,
  `shortened_url` varchar(128) NOT NULL,
  `duration_days` int(11) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `BSU_CODE` (`code`)
);

CREATE TABLE `backend_subscription_user_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `backend_subscription_user_id` int(11) NOT NULL,
  `mobile` varchar(16) NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `code` varchar(16) NOT NULL,
  `shortened_url` varchar(128) NOT NULL,
  `duration_days` int(11) NOT NULL,
  `completed` tinyint(1) NOT NULL,
  `event` varchar(64) NOT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

insert into subscription_plan values (null, "TEST PLAN", "TEST PLAN DESCRIPTION", "TIMES_PRIME", "INR", "IN", "REGULAR", now(), now(), false);
insert into subscription_plan values (null, "TEST PLAN 2", "TEST PLAN 2 DESCRIPTION", "TIMES_PRIME", "INR", "IN", "REGULAR", now(), now(), false);

insert into subscription_variant values (null, 1, "VARIANT1", "TRIAL", false, 0.0, 30, now(), now(), false);
insert into subscription_variant values (null, 1, "VARIANT2", "TRIAL_WITH_PAYMENT", false, 100.0, 100, now(), now(), false);
insert into subscription_variant values (null, 1, "VARIANT3", "PAYMENT", false, 100.0, 60, now(), now(), false);

insert into subscription_variant values (null, 2, "VARIANT4", "TRIAL", false, 0.0, 40, now(), now(), false);
insert into subscription_variant values (null, 2, "VARIANT5", "TRIAL_WITH_PAYMENT", false, 100.0, 120, now(), now(), false);
insert into subscription_variant values (null, 2, "VARIANT6", "PAYMENT", false, 100.0, 80, now(), now(), false);

insert into offer values (null, 1, "GAANA", false, "Gaana subscription", 1, now(), now(), false);
insert into offer values (null, 1, "TOI", false, "TOI subscription", 1, now(), now(), false);
insert into offer values (null, 1, "ET", false, "ET subscription", 1, now(), now(), false);

insert into offer values (null, 2, "GAANA", false, "Gaana subscription", 1, now(), now(), false);
insert into offer values (null, 2, "ET", false, "ET subscription", 1, now(), now(), false);
insert into offer values (null, 2, "TOI", false, "TOI subscription", 1, now(), now(), false);
insert into offer values (null, 2, "DINEOUT", false, "DINEOUT subscription", 1, now(), now(), false);

insert into job values (null, "SSO_COMMUNICATION_JOB", "SSO_COMMUNICATION", null, now(), now(), false);
insert into job values (null, "SUBSCRIPTION_RENEWAL_JOB", "SUBSCRIPTION_RENEWAL", null, now(), now(), false);
insert into job values (null, "SUBSCRIPTION_RENEWAL_REMINDER_JOB", "SUBSCRIPTION_RENEWAL_REMINDER", null, now(), now(), false);
insert into job values (null, "SUBSCRIPTION_EXPIRY_JOB", "SUBSCRIPTION_EXPIRY", null, now(), now(), false);
insert into job values (null, "SUBSCRIPTION_EXPIRY_REMINDER_JOB", "SUBSCRIPTION_EXPIRY_REMINDER", null, now(), now(), false);
insert into job values (null, "EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_JOB", "EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER", null, now(), now(), false);

insert into external_clients values (1, "1MG", "naVwjBtl8kX4rv9Kzu6GA7dFeWM1UpSA", "HXeIYmLvNCKjbamUnU04eOzumfsG096IYEhxFoqNCRvUYxkfN63k4584GMr8flj4", now(), now(), false);
insert into external_clients values (2, "HOUSEJOY", "UxloH5QGnVdSdICIGSCqELYcPG6w1oLu", "AZG5OJdFTTzeXeyrVhPJK1zSN7BsMaTt9oDbClF255WpfqzTJRidFC2HRBWZmPvY", now(), now(), false);
insert into external_clients values (3, "DINEOUT", "qfLKutHHwZyIYpJUEQQXpl48CG4fiNK3", "kYGMN7p0nG7bvjK1pEKQD2LbMcLY575mCJHKnD4vo0WzjmIR1CGdfquyo9q9LGvs", now(), now(), false);
insert into external_clients values (4, "OLA", "aNYwawgzNHpORkrKmGzhwz81O0rKbljU", "6o6RKuv6TnmEUZCcoyocyothlS0DH09WZRRqCV4Iut0i8l7B6r3EBCKYQOuGqbSn", now(), now(), false);
insert into external_clients values (5, "BIGBASKET", "Om1omHMstMLyIfGYTt4Y4UHHwkqemcnp", "DzSXz1aJJGmDQVbKYpmSLMNvKlNS1zA6cXgtArQD0wNRXISRdopVEV37t5iBKHoy", now(), now(), false);
insert into external_clients values (6, "QUIRK", "4M5st2SB6MiwiwPXFtbHCqxAlMlEATI6", "fRcTL356OqPJzoueHWvZ0vyiEFdRvM9EsXh82oVy0an2OmMUwNTIPqVmJolnAhjd", now(), now(), false);

alter table user add INDEX `INDEX_USER_BLOCKED` (`blocked`);
alter table user add INDEX `INDEX_USER_DELETED` (`deleted`);
alter table subscription_plan add INDEX `INDEX_SP_DELETED` (`deleted`);
alter table subscription_variant add INDEX `INDEX_SV_DELETED` (`deleted`);
alter table user_subscription add INDEX `INDEX_US_DELETED` (`deleted`);
alter table job add INDEX `INDEX_JOB_DELETED` (`deleted`);

alter table user add column `prime_id` varchar(32) NOT NULL after mobile;
alter table user_audit add column `prime_id` varchar(32) NOT NULL after mobile;
alter table user_audit add key `INDEX_PRIME_ID` (`prime_id`);

ALTER TABLE user_subscription ADD COLUMN `status_published` tinyint(1) NOT NULL after sso_communicated;
ALTER TABLE user_subscription_audit ADD COLUMN `status_published` tinyint(1) NOT NULL after sso_communicated;
ALTER TABLE user_subscription ADD INDEX `INDEX_STATUS_PUBLISHED` (`status_published`);
insert into job values (null, "USER_STATUS_PUBLISH_JOB", "USER_STATUS_PUBLISH", null, now(), now(), false);

ALTER TABLE user_subscription ADD COLUMN `status_date` datetime after status;
ALTER TABLE user_subscription_audit ADD COLUMN `status_date` datetime after status;

alter table user_subscription add column `pg_amount` double DEFAULT 0 after payment_reference;
alter table user_subscription add column `tp_reference` varchar(128) DEFAULT NULL after pg_amount;
alter table user_subscription add column `tp_amount` double DEFAULT 0 after tp_reference;
alter table user_subscription add column `promo_code` varchar(128) DEFAULT NULL after tp_amount;
alter table user_subscription add column `promo_amount` double DEFAULT 0 after promo_code;

alter table user_subscription_audit add column `pg_amount` double DEFAULT 0 after payment_reference;
alter table user_subscription_audit add column `tp_reference` varchar(128) DEFAULT NULL after pg_amount;
alter table user_subscription_audit add column `tp_amount` double DEFAULT 0 after tp_reference;
alter table user_subscription_audit add column `promo_code` varchar(128) DEFAULT NULL after tp_amount;
alter table user_subscription_audit add column `promo_amount` double DEFAULT 0 after promo_code;

alter table user_subscription modify column `ticket_id` varchar(64) DEFAULT NULL;
alter table user_subscription_audit modify column `ticket_id` varchar(64) DEFAULT NULL;


alter table user_subscription add column `price` DECIMAL(5,2) DEFAULT NULL;
alter table user_subscription_audit add column `price` DECIMAL(5,2) DEFAULT NULL;
update user_subscription set price = 0 where subscription_variant_id = 1;
update user_subscription set price = 999 where subscription_variant_id = 2;
update user_subscription_audit set price = 0 where subscription_variant_id = 1;
update user_subscription_audit set price = 999 where subscription_variant_id = 2;
alter table user_subscription modify column `price` DECIMAL(5,2) NOT NULL;
alter table user_subscription_audit modify column `price` DECIMAL(5,2) NOT NULL;
insert into subscription_property(key_name , key_value , created, updated, deleted) values("SUBSCRIPTION_CTA_RENEWAL_REMINDER_DAYS", 30, now(), now(),0);


CREATE TABLE `poc_gaana_songs` (
  `track_id` int(11) DEFAULT NULL,
  `isrc_code` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `track_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `album_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `release_date` timestamp DEFAULT CURRENT_TIMESTAMP,
  `singer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `composer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actor` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actress` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `language` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parental_advisory` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lyricist` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `album_thumbnail_path` varchar(200) DEFAULT NULL,
  `genres` varchar(100) DEFAULT NULL,
  `youtube_id` varchar(50) DEFAULT NULL,
  `popularity_index` int(11) NOT NULL,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  `valid` tinyint(1) DEFAULT NULL,
  `thumbnail` varchar(512) DEFAULT NULL,
  `max_resolution_thumbnail` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_POPULARITY` (`popularity_index`),
  KEY `KEY_TRACK_TITLE` (`track_title`),
  KEY `KEY_ISRC` (`isrc_code`),
  KEY `KEY_ALBUM` (`album_title`),
  KEY `KEY_language` (`language`),
  KEY `KEY_YOUTUBE_ID` (`youtube_id`)
);

CREATE TABLE `yt_search_results`(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gaana_id` int(11) NOT NULL,
  `title` varchar(512) NOT NULL,
  `url` varchar(512) NOT NULL,
  `view_count` int(11) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `publisher` varchar(32) NOT NULL,
  `channel_verified` TINYINT(1) DEFAULT 0,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `INDEX_GAANA` (`gaana_id`),
  INDEX `INDEX_URL` (`url`),
  INDEX `INDEX_VIEW_COUNT` (`view_count`),
  INDEX `INDEX_LIKE_COUNT` (`like_count`),
  INDEX `INDEX_VERIFIED` (`channel_verified`),
  CONSTRAINT `FK_GAANA` FOREIGN KEY (`gaana_id`) REFERENCES `poc_gaana_songs` (`id`)
);

CREATE TABLE `poc_gaana_songs_manual_from_sheets` (
  `track_id` int(11) DEFAULT NULL,
  `isrc_code` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `track_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `album_title` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `release_date` datetime NOT NULL,
  `singer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `composer` text,
  `actor` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actress` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `language` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parental_advisory` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lyricist` text,
  `label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `album_thumbnail_path` text,
  `genres` varchar(100) DEFAULT NULL,
  `youtube_id` varchar(512) DEFAULT NULL,
  `popularity_index` int(11) DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL,
  `valid` tinyint(1) DEFAULT NULL,
  `thumbnail` varchar(512) DEFAULT NULL,
  `max_resolution_thumbnail` varchar(512) DEFAULT NULL,
  `album_release_date` datetime DEFAULT NULL,
  `s3_album_thumbnail_path` text,
  `s3_video_thumbnail_path` text,
  `job_tag` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_POPULARITY` (`popularity_index`),
  KEY `KEY_TRACK_TITLE` (`track_title`(191)),
  KEY `KEY_ISRC` (`isrc_code`),
  KEY `KEY_ALBUM` (`album_title`(191)),
  KEY `KEY_language` (`language`(191)),
  KEY `KEY_YOUTUBE_ID` (`youtube_id`),
  KEY `track_id_key` (`track_id`),
  KEY `KEY_JOB_TAG` (`job_tag`)
);

UPDATE poc_gaana_songs_manual_from_sheets t1 INNER JOIN tg_album_enrich t2 ON t1.track_id = t2.track_id SET t1.track_title = t2.track_title, t1.album_title = t2.album_title,
 t1.release_date = t2.release_date, t1.singer = t2.singer, t1.composer = t2.composer, t1.actor = t2.actor, t1.actress = t2.actress, t1.language = t2.language, t1.label = t2.label,
 t1.album_thumbnail_path = t2.album_thumbnail_path, t1.genres = t2.genres, t1.album_release_date = t2.release_date;