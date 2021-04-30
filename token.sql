SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS 'tb_coin_config';
CREATE TABLE  'tb_coin_config' (
    'id' int(11) NOT NULL  AUTO_INCREMENT,
    'biz_type' varchar(255) NOT NULL COMMENT '业务类型',
    'coin_total' int(11) NOT NULL COMMENT '币的总价',
    'coin_reserved' int(11) NOT NULL COMMENT '机构预留币的数量',
    'coin_per_deal' double NOT NULL COMMENT '每笔交易最小的币支付金额',
    'coin_block_create' double NOT NULL COMMENT '生成一个区块的奖励币数量',
    'create_time' datetime NOT NULL COMMENT '规则配置时间',
    'update_time' timestamp NOT NULL COMMENT DEFAULT CURRENT_TIMESTAMP ON UPDATE
        CURRENT_TIMESTAMP  COMMENT '规则修改时间',
    PRIMARY KEY ('id')
) ENIGINE=InnoDB DEFAULT CHARACTER=utf8mb4;