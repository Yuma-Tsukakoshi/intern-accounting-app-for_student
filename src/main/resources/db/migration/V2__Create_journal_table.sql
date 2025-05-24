CREATE TABLE PUBLIC.journals
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    date                DATE NOT NULL,
    summary             VARCHAR(200)
);

CREATE TABLE PUBLIC.journal_details
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    debit_credit_type   VARCHAR(20) NOT NULL ,
    amount              BIGINT,
    account_code        CHAR(4) NOT NULL,
    journal_id          INT NOT NULL,
    FOREIGN KEY (journal_id) REFERENCES journals(id),
    FOREIGN KEY (account_code) REFERENCES accounts(account_code)
);

INSERT INTO public.journals(id, date, summary) VALUES
                                                   (1, '2024-01-01', '1000円の商品を売り上げて、売り上げを現金で受け取った'),
                                                   (2, '2024-01-03', '2000円の商品を売り上げて、売り上げを現金で受け取った'),
                                                   (3, '2024-01-19', '2000円の預金利息を預金（銀行振込）で受け取った'),
                                                   (4, '2024-01-20', '給料500円を現金で支払った。その際手数料は100円かかった'),
                                                   (5, '2024-01-20', '2000円を借りて、現金で受け取った'),
                                                   (6, '2024-01-21', '1000000002の仕訳が誤りだったので修正した'),
                                                   (7, '2024-01-21', '1000000002の正しい仕訳は、「1500円の預金利息を預金（銀行振込）で受け取った」であった')
;

INSERT INTO public.journal_details(journal_id, account_code, debit_credit_type, amount) VALUES
                                                                                            -- 仕訳1
                                                                                            (1, '3001', 'DEBIT',  1000),  -- 現金
                                                                                            (1, '1001', 'CREDIT', 1000),  -- 売上高

                                                                                            -- 仕訳2
                                                                                            (2, '3001', 'DEBIT',  2000),  -- 現金
                                                                                            (2, '1001', 'CREDIT', 2000),  -- 売上高

                                                                                            -- 仕訳3
                                                                                            (3, '3002', 'DEBIT',  2000),  -- 預金
                                                                                            (3, '1011', 'CREDIT', 2000),  -- 預金利息

                                                                                            -- 仕訳4（給料＋手数料→現金600）
                                                                                            (4, '2001', 'DEBIT',   500),  -- 給料
                                                                                            (4, '2003', 'DEBIT',   100),  -- 支払手数料
                                                                                            (4, '3001', 'CREDIT',  600),  -- 現金

                                                                                            -- 仕訳5
                                                                                            (5, '3001', 'DEBIT',  2000),  -- 現金
                                                                                            (5, '4001', 'CREDIT', 2000),  -- 借入金

                                                                                            -- 仕訳6（逆仕訳）
                                                                                            (6, '1011', 'DEBIT',  2000),  -- 預金利息
                                                                                            (6, '3002', 'CREDIT', 2000),  -- 預金

                                                                                            -- 仕訳7（訂正仕訳）
                                                                                            (7, '3002', 'DEBIT',  1500),  -- 預金
                                                                                            (7, '1011', 'CREDIT', 1500)   -- 預金利息
;
ALTER TABLE PUBLIC.journals ALTER COLUMN id RESTART WITH 8;
ALTER TABLE PUBLIC.journal_details ALTER COLUMN id RESTART WITH 16;