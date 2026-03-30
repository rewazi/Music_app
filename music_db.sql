-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Мар 30 2026 г., 21:59
-- Версия сервера: 10.4.32-MariaDB
-- Версия PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `music_db`
--

-- --------------------------------------------------------

--
-- Структура таблицы `albums`
--

CREATE TABLE `albums` (
  `id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `singer_id` int(11) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `banner_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `albums`
--

INSERT INTO `albums` (`id`, `title`, `singer_id`, `image_url`, `banner_url`) VALUES
(1, 'After Hours', 1, 'https://via.placeholder.com/300/E8622A/FFFFFF?text=After+Hours', 'https://via.placeholder.com/600x220/E8622A/FFFFFF?text=The+Weeknd+Banner'),
(2, 'Mercury', 2, 'https://via.placeholder.com/300/4A1535/FFFFFF?text=Mercury', 'https://via.placeholder.com/600x220/4A1535/FFFFFF?text=Imagine+Dragons+Banner'),
(3, 'AM', 3, 'https://via.placeholder.com/300/E8622A/FFFFFF?text=AM', 'https://via.placeholder.com/600x220/E8622A/FFFFFF?text=Arctic+Monkeys+Banner'),
(4, 'Future Nostalgia', 4, 'https://via.placeholder.com/300/4A1535/FFFFFF?text=Nostalgia', 'https://via.placeholder.com/600x220/4A1535/FFFFFF?text=Dua+Lipa+Banner');

-- --------------------------------------------------------

--
-- Структура таблицы `singers`
--

CREATE TABLE `singers` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `singers`
--

INSERT INTO `singers` (`id`, `name`) VALUES
(1, 'The Weeknd'),
(2, 'Imagine Dragons'),
(3, 'Arctic Monkeys'),
(4, 'Dua Lipa');

-- --------------------------------------------------------

--
-- Структура таблицы `songs`
--

CREATE TABLE `songs` (
  `id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `singer_id` int(11) DEFAULT NULL,
  `album_id` int(11) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `banner_url` varchar(255) DEFAULT NULL,
  `song_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `songs`
--

INSERT INTO `songs` (`id`, `title`, `singer_id`, `album_id`, `image_url`, `banner_url`, `song_url`) VALUES
(1, 'Blinding Lights', 1, 1, 'https://via.placeholder.com/300', 'https://via.placeholder.com/600x220', 'http://10.0.2.2/musicapp/songs/lights.mp3'),
(2, 'Believer', 2, 2, 'https://via.placeholder.com/300', 'https://via.placeholder.com/600x220', 'http://10.0.2.2/musicapp/songs/believer.mp3');

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`) VALUES
(1, 'Test', 'test@test.com', '$2y$10$G8dtAc1TuK3YbheOo2wAwuIvsVy1fjW810R5SVv2hJ9P5.4jf9jNC'),
(2, '123', '123@test.com', '$2y$10$CvSo9jhWMws5lYuYyU8cAOqScPpaAg3IEeVknnYNpAKI9wMthHR.O');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `albums`
--
ALTER TABLE `albums`
  ADD PRIMARY KEY (`id`),
  ADD KEY `singer_id` (`singer_id`);

--
-- Индексы таблицы `singers`
--
ALTER TABLE `singers`
  ADD PRIMARY KEY (`id`);

--
-- Индексы таблицы `songs`
--
ALTER TABLE `songs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `singer_id` (`singer_id`),
  ADD KEY `album_id` (`album_id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `albums`
--
ALTER TABLE `albums`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `singers`
--
ALTER TABLE `singers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT для таблицы `songs`
--
ALTER TABLE `songs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `albums`
--
ALTER TABLE `albums`
  ADD CONSTRAINT `albums_ibfk_1` FOREIGN KEY (`singer_id`) REFERENCES `singers` (`id`);

--
-- Ограничения внешнего ключа таблицы `songs`
--
ALTER TABLE `songs`
  ADD CONSTRAINT `songs_ibfk_1` FOREIGN KEY (`singer_id`) REFERENCES `singers` (`id`),
  ADD CONSTRAINT `songs_ibfk_2` FOREIGN KEY (`album_id`) REFERENCES `albums` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
