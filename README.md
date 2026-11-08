# Kriptan

Лабораторные работы по курсу «Кроссплатформенная разработка», трек Compose Multiplatform.
Одно приложение растёт от вехи к вехе.

## Предметная область

Kriptan — каталог криптовалют. В нём два экрана:

- **Список монет** — по рыночной капитализации, страницами по 50: тикер, название, объём торгов,
  цена и изменение за 24 часа. Следующая страница подгружается при прокрутке. Есть поиск по
  названию или тикеру среди загруженных монет.
- **Монета** — подробности: ранг, цена, капитализация, объём, максимум и минимум за 24 часа,
  исторический максимум, предложение (в обращении, всего, максимум), описание, время обновления.

Тему (тёмная/светлая) и язык интерфейса (русский/английский) можно переключить в шапке списка.
Переводится интерфейс, данные каталога остаются как в источнике.

## Откуда данные

Источник — публичный [CoinGecko API](https://docs.coingecko.com/reference/introduction), без ключа:

- `GET coins/markets?vs_currency=usd&page=N&per_page=50` — список по страницам;
- `GET coins/{id}` — экран монеты, свой запрос.

Цепочка: `HttpClient` (Ktor, один на приложение) → `CoinGeckoApi` (один метод на адрес, возвращает
DTO) → `NetworkCoinRepository` (DTO → модель domain, ошибки → `LoadError`, разбор на
`Dispatchers.Default`) → ViewModel (`StateFlow` состояния экрана) → экран. Движок HTTP-клиента
свой под каждую платформу: CIO на Android и Desktop, Darwin на iOS (`expect fun httpEngine()`).

Адрес API в коде не записан: он лежит в `gradle.properties` (`coingecko.baseUrl`) и попадает в код
через BuildKonfig. Для одного запуска его можно подменить, например чтобы увидеть экран ошибки:

```bash
./gradlew :desktopApp:run -Pcoingecko.baseUrl=https://api.coingecko.invalid/api/v3/
```

Бесплатный CoinGecko пропускает 5–15 запросов в минуту; при превышении приложение показывает
«Слишком много запросов» и кнопку «Повторить».

## Запуск

Нужен JDK (подойдёт встроенный в Android Studio). Команды выполняются из корня проекта.

| Таргет  | Команда |
|---------|---------|
| Desktop | `./gradlew :desktopApp:run` |
| Android | `./gradlew :androidApp:installDebug` — при запущенном эмуляторе или подключённом устройстве |
| iOS     | открыть `iosApp/iosApp.xcodeproj` в Xcode, схема `iosApp`, запустить на симуляторе |

Проверка, что ключи строк совпадают в обеих локалях:

```bash
python3 tools/check-strings.py
```

## Структура

```
shared/src/commonMain/kotlin/com/ellert/kriptan/
  domain/   модели Coin, CoinDetail, ошибки LoadError, интерфейс CoinRepository
  data/     network/ (HttpClient, CoinGeckoApi), dto/, mapper/, NetworkCoinRepository
  ui/       экраны (list, detail) с ViewModel и состоянием, навигация, тема, формат чисел, смена языка
  App.kt    корень: состояние приложения и связь слоёв
shared/src/commonMain/composeResources/   строки (en, ru) и иконки
androidApp/, desktopApp/, iosApp/         точки входа платформ
```

Стек: Kotlin Multiplatform, Compose Multiplatform, Material 3, Navigation 3, Ktor, kotlinx.serialization,
kotlinx.coroutines (StateFlow), BuildKonfig.
