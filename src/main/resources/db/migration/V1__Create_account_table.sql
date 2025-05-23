CREATE TABLE PUBLIC.accounts
(
    account_code        VARCHAR(30) NOT NULL,
    name                VARCHAR(50) NOT NULL,
    account_type        VARCHAR(20) NOT NULL,
    parent_account_code VARCHAR(30),
    PRIMARY KEY (account_code)
);

-- 収益の科目
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('1000', '営業利益', 'PROFIT', NULL);
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('1001', '売上高', 'PROFIT', '1000');
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('1010', '営業外利益', 'PROFIT', NULL);
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('1011', '預金利息', 'PROFIT', '1010');
-- 費用の科目
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('2000', '経費', 'LOSS', NULL);
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('2001', '給料', 'LOSS', '2000');
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('2002', '交通費', 'LOSS', '2000');
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('2003', '支払手数料', 'LOSS', '2000');
-- 資産の科目
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('3000', '流動資産', 'ASSET', NULL);
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('3001', '現金', 'ASSET', '3000');
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('3002', '預金', 'ASSET', '3000');
-- 負債の科目
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('4000', '流動負債', 'LIABILITY', NULL);
INSERT INTO accounts(account_code, name, account_type, parent_account_code) VALUES ('4001', '借入金', 'LIABILITY', '4000');
