# tg-bot
Задача 6
Реализация системы отзывов
Пример
П) Отзывы
Б) Выберите опцию - Написать отзыв, Посмотреь отзывы
П) Написать отзыв
Б) Введите название модели, находящуюся в нашем магазине, на которую, хотите написать отзыв:
Asus Rog 5
Xiaomi Poco F4
П) Asus Rog 5
Б) Напишите отзыв
П) Телефон хороший
П) Да
Б) Отзыв записан
П) Посмотреть отзывы
Б) Введите название модели, находящуюся в нашем магазине, на которую, хотите посмотреть отзывы:
Asus Rog 5
Xiaomi Poco F4
Б) Asus Rog 5
Б) Телефон хороший

## Инструкция по деплою бота на сервер:

1)Подключаемся по ssh к vds 

2)Устанавливаем java: ```sudo apt install default-jdk```

3)Переносим файлы нашего проекта на сервер через scp: ```scp -r /mnt/c/Users/Admin/Desktop/tg-bot root@176.124.206.9:~/jb```

4)Скачиваем maven: ```sudo apt install maven```

5)Билдим проект и запускаем утилитой maven 

команда запуска: ```root@v1271736:~/jb# ./apache-maven-3.8.6/bin/mvn exec:java -Dexec.mainClass="org.example.Main"```