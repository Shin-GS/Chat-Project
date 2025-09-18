-- Insert packs (id is auto-increment)
INSERT INTO STICKER_PACK_M (CATEGORY, NAME, REG_DTM, MOD_DTM) VALUES ('animals', 'Animals', NOW(), NOW());
INSERT INTO STICKER_PACK_M (CATEGORY, NAME, REG_DTM, MOD_DTM) VALUES ('bears', 'Bears', NOW(), NOW());
INSERT INTO STICKER_PACK_M (CATEGORY, NAME, REG_DTM, MOD_DTM) VALUES ('foods', 'Foods', NOW(), NOW());

-- Insert stickers (use subquery to resolve PACK_ID by CATEGORY)
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'cat_wave', 'http://localhost:8080/sticker/animals/cat_wave.png', 'cat wave', 1, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'dog_laugh', 'http://localhost:8080/sticker/animals/dog_laugh.png', 'dog laugh', 2, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'fox_hi', 'http://localhost:8080/sticker/animals/fox_hi.png', 'fox hi', 3, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'panda_sleep', 'http://localhost:8080/sticker/animals/panda_sleep.png', 'panda sleep', 4, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'rabbit_heart', 'http://localhost:8080/sticker/animals/rabbit_heart.png', 'rabbit heart', 5, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'lion_roar', 'http://localhost:8080/sticker/animals/lion_roar.png', 'lion roar', 6, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'tiger_dance', 'http://localhost:8080/sticker/animals/tiger_dance.png', 'tiger dance', 7, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_wave', 'http://localhost:8080/sticker/animals/bear_wave.png', 'bear wave', 8, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'penguin_slide', 'http://localhost:8080/sticker/animals/penguin_slide.png', 'penguin slide', 9, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'koala_hug', 'http://localhost:8080/sticker/animals/koala_hug.png', 'koala hug', 10, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'animals';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_hello', 'http://localhost:8080/sticker/bears/bear_hello.png', 'bear hello', 1, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_laugh', 'http://localhost:8080/sticker/bears/bear_laugh.png', 'bear laugh', 2, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_sad', 'http://localhost:8080/sticker/bears/bear_sad.png', 'bear sad', 3, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_angry', 'http://localhost:8080/sticker/bears/bear_angry.png', 'bear angry', 4, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_ok', 'http://localhost:8080/sticker/bears/bear_ok.png', 'bear ok', 5, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_think', 'http://localhost:8080/sticker/bears/bear_think.png', 'bear think', 6, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_sleep', 'http://localhost:8080/sticker/bears/bear_sleep.png', 'bear sleep', 7, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_eat', 'http://localhost:8080/sticker/bears/bear_eat.png', 'bear eat', 8, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_heart', 'http://localhost:8080/sticker/bears/bear_heart.png', 'bear heart', 9, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'bear_wave', 'http://localhost:8080/sticker/bears/bear_wave.png', 'bear wave', 10, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'bears';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'pizza_yum', 'http://localhost:8080/sticker/foods/pizza_yum.png', 'pizza yum', 1, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'burger_happy', 'http://localhost:8080/sticker/foods/burger_happy.png', 'burger happy', 2, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'donut_sprinkle', 'http://localhost:8080/sticker/foods/donut_sprinkle.png', 'donut sprinkle', 3, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'taco_party', 'http://localhost:8080/sticker/foods/taco_party.png', 'taco party', 4, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'sushi_love', 'http://localhost:8080/sticker/foods/sushi_love.png', 'sushi love', 5, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'coffee_energy', 'http://localhost:8080/sticker/foods/coffee_energy.png', 'coffee energy', 6, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'tea_relax', 'http://localhost:8080/sticker/foods/tea_relax.png', 'tea relax', 7, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'cake_bday', 'http://localhost:8080/sticker/foods/cake_bday.png', 'cake bday', 8, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'icecream_cool', 'http://localhost:8080/sticker/foods/icecream_cool.png', 'icecream cool', 9, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';
INSERT INTO STICKER_D (PACK_ID, STICKER_CODE, IMAGE_URL, ALT_TEXT, SORT_NUMBER, REG_DTM, MOD_DTM)
SELECT p.PACK_ID, 'hotdog_fun', 'http://localhost:8080/sticker/foods/hotdog_fun.png', 'hotdog fun', 10, NOW(), NOW()
FROM STICKER_PACK_M p WHERE p.CATEGORY = 'foods';