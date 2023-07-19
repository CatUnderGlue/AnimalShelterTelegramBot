<image src="https://cs11.pikabu.ru/post_img/big/2020/07/30/6/1596102657183361808.png" alt="logo.png">

# Информация

**Авторы:**<br>
Максим Сыров ([CatUnderGlue](https://github.com/CatUnderGlue))<br>
Ильяс Кучукбаев ([Ilyas344](https://github.com/Ilyas344))<br>
Исакова Мария ([profmi2022](https://github.com/profmi2022))<br>
Ильдар Губайдуллин ([MexxMo](https://github.com/MexxMo))<br>

## Проект: Телеграм бот для приюта котов или собак<br>

## Старт проекта: 05.04.2023<br>

## Описание: 
Чат-бот для приюта животных, который предоставляет информацию о приюте, консультирует потенциальных хозяев животных и ведёт отчеты о питомцах. Бот предоставляет вводную информацию о приюте, его расположении, режиме работы и правила. Также он помогает потенциальным хозяевам собак разобраться с бюрократическими и бытовыми вопросами, связанными с усыновлением. После усыновления, бот присылать форму ежедневного отчета, включающую фото, рацион, общее самочувствие и изменения в поведении питомца. Волонтеры приюта могут принимать решение о дальнейшей судьбе собаки на основе этих отчетов.<br>

# Запуск приложения
+ Перед запуском приложения _обязательно_ добавьте эти переменные в параметры запуска:
  * db.url = ссылка на подключение к вашей бд
  * db.user = ваш пользователь бд
  * db.password = ваш пароль от бд
  * telegram.bot.token = токен от вашего телеграм бота
+ Можно запускать главный класс приложения.
  
# Стэк технологий
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white "Java 11")
![Maven](https://img.shields.io/badge/Maven-green.svg?style=for-the-badge&logo=mockito&logoColor=white "Maven")
![Spring](https://img.shields.io/badge/Spring-blueviolet.svg?style=for-the-badge&logo=spring&logoColor=white "Spring")
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![GitHub](https://img.shields.io/badge/git-%23121011.svg?style=for-the-badge&logo=github&logoColor=white "Git")
+ ЯП: *Java 17*
+ Автоматизация сборки: *Maven*
+ Фреймворк: *Spring*
+ База данных: *PostgreSQL*
+ Контроль версий: *Git*
+ Работа с телеграм: *com.github.pengrad*
