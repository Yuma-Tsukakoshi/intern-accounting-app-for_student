CREATE TABLE PUBLIC.accounts
(
    account_code CHAR(4)    NOT NULL,
    name         VARCHAR(50) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (account_code)
);

-- 収益の科目
INSERT INTO accounts(account_code, name, account_type) VALUES ('1000', '営業利益', 'PROFIT');
INSERT INTO accounts(account_code, name, account_type) VALUES ('1001', '売上高', 'PROFIT');
INSERT INTO accounts(account_code, name, account_type) VALUES ('1010', '営業外利益', 'PROFIT');
INSERT INTO accounts(account_code, name, account_type) VALUES ('1011', '預金利息', 'PROFIT');

-- 費用の科目
INSERT INTO accounts(account_code, name, account_type) VALUES ('2000', '経費', 'LOSS');
INSERT INTO accounts(account_code, name, account_type) VALUES ('2001', '給料', 'LOSS');
INSERT INTO accounts(account_code, name, account_type) VALUES ('2002', '交通費', 'LOSS');
INSERT INTO accounts(account_code, name, account_type) VALUES ('2003', '支払手数料', 'LOSS');

-- 資産の科目
INSERT INTO accounts(account_code, name, account_type) VALUES ('3000', '流動資産', 'ASSET');
INSERT INTO accounts(account_code, name, account_type) VALUES ('3001', '現金', 'ASSET');
INSERT INTO accounts(account_code, name, account_type) VALUES ('3002', '預金', 'ASSET');

-- 負債の科目
INSERT INTO accounts(account_code, name, account_type) VALUES ('4000', '流動負債', 'LIABILITY');
INSERT INTO accounts(account_code, name, account_type) VALUES ('4001', '借入金', 'LIABILITY');
