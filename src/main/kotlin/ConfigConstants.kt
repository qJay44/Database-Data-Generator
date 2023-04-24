open class ConfigConstants {
    protected companion object {
        val userGroups: Array<String> = arrayOf(
            "Student", "Teacher"
        )

        val groupNames1: Array<String> = arrayOf(
            "ИД 23.1/Б1-19",
            "ИД 23.2/Б1-19",
            "ИД 23.3/Б1-19",
        )

        val groupNames2: Array<String> = arrayOf(
            "ЭД 23.1/Б1-19",
            "ЭД 23.2/Б1-19",
            "ЭД 23.3/Б1-19",
        )

        val timeStart: Array<String> = arrayOf(
            "08:20", "10:00", "11:40", "13:45", "15:25", "17:05"
        )

        val timeEnd: Array<String> = arrayOf(
            "09:50", "11:30", "13:10", "15:15", "16:55", "18:35"
        )

        val scheduleTypes: Array<String> = arrayOf(
            "Лекция", "СПЗ"
        )

        @Suppress("SpellCheckingInspection")
        const val SENTENCE =
            "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque " +
                    "laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi " +
                    "architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas " +
                    "sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione " +
                    "voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit " +
                    "amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut " +
                    "labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis " +
                    "nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea " +
                    "commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit" +
                    "esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"

        val subjectNames1: Array<String> = arrayOf(
            "Адаптация типовых конфигураций корпоративных информационных систем",
            "Библиотеки стандартных подсистем",
            "Информационная безопасность",
            "Информационные системы и технологии",
            "Обмен данными в корпоративных информационных системах",
            "Реплицированные распределенные хранилища данных",
            "Управление данными в корпоративных информационных системах",
            "Цифровой маркетинг"
        )

        val subjectNames2: Array<String> = arrayOf(
            "Теория бухгалтерского учета",
            "Налоги и налогообложение",
            "Бухгалтерский финансовый учет",
            "Бухгалтерский управленческий учет",
            "Лабораторный практикум по бухгалтерскому учету",
            "Бухгалтерские информационные системы",
            "Бухгалтерская (финансовая) отчетность",
            "Антикризисное управление",
            "Аудит",
            "Комплексный экономический анализ хозяйственной деятельности"
        )

        val examTypes: Array<String> = arrayOf(
            "Зачет", "Экзамен"
        )
    }
}
