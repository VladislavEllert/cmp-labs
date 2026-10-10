# Kriptan

Лабораторные работы по курсу «Кроссплатформенная разработка», трек Compose Multiplatform.
Одно приложение растёт от вехи к вехе.

## Предметная область

Kriptan — каталог криптовалют. В нём два экрана:

- **Список монет** — топ-25 по рыночной капитализации: тикер, название, объём торгов,
  цена и изменение за 24 часа. Есть поиск по названию или тикеру.
- **Монета** — подробности: ранг, цена, капитализация, объём, максимум и минимум за 24 часа,
  исторический максимум, предложение (в обращении, всего, максимум), время обновления.

Тему (тёмная/светлая) и язык интерфейса (русский/английский) можно переключить в шапке списка.
Переводится интерфейс, данные каталога остаются как в источнике.

## Откуда данные

Источник — [CoinGecko API](https://docs.coingecko.com/reference/coins-markets),
запрос `GET /coins/markets?vs_currency=usd`.

В вехе B1 сети нет: список собран из моков. Это 25 записей, снятых с настоящего ответа API
25.09.2026, с теми же полями, что отдаёт источник. Моки лежат в
`shared/src/commonMain/kotlin/com/ellert/kriptan/data/MockCoinRepository.kt`. Экраны получают
данные через интерфейс `CoinRepository`, поэтому в B2 моки заменяются сетью без правок в UI.

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
  domain/   модель Coin и интерфейс CoinRepository
  data/     MockCoinRepository — реализация на моках
  ui/       экраны (list, detail), навигация, тема, формат чисел, смена языка
  App.kt    корень: состояние приложения и связь слоёв
shared/src/commonMain/composeResources/   строки (en, ru) и иконки
androidApp/, desktopApp/, iosApp/         точки входа платформ
```

Стек: Kotlin Multiplatform, Compose Multiplatform, Material 3, Navigation 3.
