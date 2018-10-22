CREATE TABLE `temperature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '1开盘，2收盘，3正常，',
  `day_format` varchar(10) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `yesterday_show` int(10) DEFAULT NULL COMMENT '展示除以100',
  `now_temperature` int(10) DEFAULT NULL COMMENT '当时温度',
  `raise_up` int(11) DEFAULT NULL,
  `down_up` int(11) DEFAULT NULL,
  `open` int(11) DEFAULT NULL COMMENT '炸板',
  `raise` int(11) DEFAULT NULL,
  `down` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
CREATE TABLE `mystock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `day_format` varchar(10) NOT NULL COMMENT 'yyyy-mm-dd',
  `code` varchar(8) NOT NULL,
  `name` varchar(8) DEFAULT NULL,
  `today_open_price` varchar(10) DEFAULT NULL,
  `today_close_price` varchar(10) DEFAULT NULL,
  `tomorrow_open_price` varchar(10) DEFAULT NULL,
  `tomorrow_close_price` varchar(10) DEFAULT NULL,
  `yesterday_close_price` varchar(10) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`day_format`,`code`),
  KEY `Index_day` (`day_format`),
  KEY `day_format` (`day_format`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
