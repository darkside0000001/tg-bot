# tg-bot
Задача 5
Реализация системы скидок и оповещение пользователя о скидках. Пользователь может сам посмотреть скидки, использую команду /discounts или включить уведомления о скидках, используя командy /notfication, тогда бот будет сам уведомлять пользователя о скидках. Пользователь может сам выбирать интервал частоты появления уведомлений
Пример
П)/discounts
Б)Сегодня у нас продается с 50% скидкой:
HP15s-eq1332ur
П)/notification
Б)Хотите получать уведомления о скидках (введите Yes или No)
П)Yes
Б)Выберите частоту появления уведомлений:
1)30 сек
2)24 часа
3)неделя
П)1
Б)Уведомления подключены
Б)Сегодня у нас продается с 50% скидкой: HP 15s-eq1332ur

Инстркция по деплою бота на сервер:
Подключаемся по ssh к vds
через scp переносим туда файлы нашего проекта
билдим проект и запускаем утилитой maven
команда запуска:   root@v1271736:~/jb# ./apache-maven-3.8.6/bin/mvn exec:java -Dexec.mainClass="org.example.Main"
