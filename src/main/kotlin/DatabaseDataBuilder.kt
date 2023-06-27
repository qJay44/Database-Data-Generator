import com.google.gson.*
import model.*
import java.io.FileWriter
import java.io.PrintWriter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.text.SimpleDateFormat
import java.util.*

object DatabaseDataBuilder : ConfigConstants() {

    private val people by lazy {
        val client = HttpClient.newBuilder().build()
        val params = "LastName,FirstName,FatherName,Login,Password"
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.randomdatatools.ru/?count=100&params=$params"))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        Gson().fromJson(response.body(), Array<Person>::class.java).toMutableList()
    }

    private val groupsMap = mutableMapOf<String, List<Subject>>()

    val studentList: MutableList<Student> = mutableListOf()
    val teacherList: MutableList<Teacher> = mutableListOf()
    val scheduleList: MutableList<Schedule> = mutableListOf()
    val notificationList: MutableList<Notification> = mutableListOf()
    val profileAttendanceList: MutableList<ProfileAttendance> = mutableListOf()
    val subject1List: MutableList<Subject> = mutableListOf()
    val subject2List: MutableList<Subject> = mutableListOf()

    private fun randInt(a: Int, b: Int) = (a..b).shuffled().last()
    private fun randArrayElement(arr: Array<String>) = arr[(arr.indices).shuffled().last()]

    private fun randomText(textLength: Int? = null): String {
        val textObj = object {
            private val words = SENTENCE.split(" ")
            private val length = textLength ?: (1..words.size).shuffled().last()
            var text = ""

            init {
                for (k in 0 until length) {
                    text += words.shuffled().last() + " "
                    if (textLength != null && randInt(0, 10) == 5) text += "\n"
                }
            }
        }

        return textObj.text
    }

    // Weeks to create: previous, current and next
    private fun createScheduleForThreeWeeks(currentGroupName: String) {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru"))
        val calendar = Calendar.getInstance(Locale.forLanguageTag("ru"))
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        // Set current week to previous
        calendar.add(Calendar.WEEK_OF_MONTH, -1)
        var roomCounter = 0
        for (k in 0 until 3) {
            val studyDays = randInt(1, 7)
            for (i in 0 until studyDays) {
                val amount = randInt(1, 6)
                val maxStartIndex = timeStart.size - amount
                val startIndex = randInt(0, maxStartIndex)
                val currentSubjectList = groupsMap[currentGroupName]

                for (j in 0 until amount) {
                    if (currentSubjectList.isNullOrEmpty()) {
                        println("No subject found for this group")
                        return
                    }

                    val subject = currentSubjectList[(currentSubjectList.indices).shuffled().last()]
                    val schedule = Schedule(
                        id = UUID.randomUUID(),
                        date = format.format(calendar.time),
                        timeStart = timeStart[startIndex + j],
                        timeEnd = timeEnd[startIndex + j],
                        roomNum = ++roomCounter,
                        type = randArrayElement(scheduleTypes),
                        teacherNotes = randomText(50),
                        subjectID = UUID.fromString(subject.id),
                        teacherID = UUID.fromString(subject.teacherID)
                    )

                    scheduleList += schedule
                }
                // Next day
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            // Subtract extra day
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            // Set day of week to Monday
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            // Next week
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }
    }

    private fun createNotificationsForTwoMonths() {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru"))
        val calendar = Calendar.getInstance(Locale.forLanguageTag("ru"))
        val currentDay = calendar.time

        // Set month to previous
        calendar.add(Calendar.MONTH, -1)
        var i = 0
        while (calendar.time != currentDay) {
            val notificationText = randomText()
            val groupsToUse = if (randInt(0, 1) == 0) groupNames1 else groupNames2

            val notification = Notification(
                UUID.randomUUID().toString(),
                format.format(calendar.time),
                "Уведомление №$i",
                notificationText,
                randArrayElement(groupsToUse)
            )

            notificationList += notification

            // Next day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            i++
        }
        notificationList.reverse()
    }

    private fun createProfileAttendance() {
        val boolList = listOf(false, true)
        for (student in studentList) {
            for (schedule in scheduleList.withIndex()) {
                for (pair in subject1List zip subject2List) {
                    if (student.groupName == pair.first.groupName
                        || student.groupName == pair.second.groupName) {

                        val profileAttendance = ProfileAttendance(
                            id = UUID.randomUUID().toString(),
                            scheduleID = schedule.value.id.toString(),
                            userID = student.id.toString(),
                            visited = boolList.shuffled().last()
                        )
                        profileAttendanceList += profileAttendance
                    }
                }
                // Cut search amount
                if (schedule.index >= 20) break
            }
        }
        profileAttendanceList.shuffle()
    }

    private fun createSubjects() {
        val groupIT1List = mutableListOf<Subject>()
        val groupIT2List = mutableListOf<Subject>()
        val groupIT3List = mutableListOf<Subject>()

        val groupEconomics1List = mutableListOf<Subject>()
        val groupEconomics2List = mutableListOf<Subject>()
        val groupEconomics3List = mutableListOf<Subject>()

        val unusedTeachers = mutableListOf<Teacher>()
        unusedTeachers.addAll(teacherList)

        groupNames1.zip(groupNames2).forEach rootLoop@ { (ITGroupName, EconomicGroupName) ->
            subjectNames1.zip(subjectNames2).forEach { (ITSubjectName, EconomicSubjectName) ->
                if (unusedTeachers.isEmpty()) return@rootLoop
                var teacher = unusedTeachers.shuffled().last()
                var examTypeIndex = randInt(0, 1)

                // For IT faculty
                val subject1 = Subject(
                    id = UUID.randomUUID().toString(),
                    subjectName = ITSubjectName,
                    groupName = ITGroupName,
                    teacherID = teacher.id.toString(),
                    examType = examTypes[examTypeIndex]
                )
                subject1List += subject1
                unusedTeachers.removeLast()

                if (unusedTeachers.isEmpty()) return@rootLoop
                teacher = unusedTeachers.shuffled().last()

                examTypeIndex = randInt(0, 1)
                // For Economics faculty
                val subject2 = Subject(
                    id = UUID.randomUUID().toString(),
                    subjectName = EconomicSubjectName,
                    groupName = EconomicGroupName,
                    teacherID = teacher.id.toString(),
                    examType = examTypes[examTypeIndex]
                )
                subject2List += subject2
                unusedTeachers.removeLast()
            }
        }

        for (subject in subject1List) {
            when (subject.groupName) {
                "ИД 23.1/Б1-19" -> groupIT1List.add(subject)
                "ИД 23.2/Б1-19" -> groupIT2List.add(subject)
                "ИД 23.3/Б1-19" -> groupIT3List.add(subject)
            }
        }

        for (subject in subject2List) {
            when (subject.groupName) {
                "ЭД 23.1/Б1-19" -> groupEconomics1List.add(subject)
                "ЭД 23.2/Б1-19" -> groupEconomics2List.add(subject)
                "ЭД 23.3/Б1-19" -> groupEconomics3List.add(subject)
            }
        }
        groupsMap["ИД 23.1/Б1-19"] = groupIT1List
        groupsMap["ИД 23.2/Б1-19"] = groupIT2List
        groupsMap["ИД 23.3/Б1-19"] = groupIT3List

        groupsMap["ЭД 23.1/Б1-19"] = groupEconomics1List
        groupsMap["ЭД 23.2/Б1-19"] = groupEconomics2List
        groupsMap["ЭД 23.3/Б1-19"] = groupEconomics3List
    }

    private fun writeToJSON() {
        PrintWriter(FileWriter("data.json")).use {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

            val subjects = mutableListOf<Subject>()
            subjects.addAll(subject1List)
            subjects.addAll(subject2List)

            @Suppress("unused") // Not used but must be for JSON structure
            val objects = object {
                private val students = studentList
                private val teachers = teacherList
                private val schedule = scheduleList
                private val notifications = notificationList
                private val profileAttendances = profileAttendanceList
                private val subjects = subjects
            }

            it.write(gson.toJson(objects))
        }
    }

    fun createAll(amount: Int = 100) {
        for (i in 0 until amount) {
            val isHalf = i > amount / 2 - 1

            when (if (isHalf) userGroups[0] else userGroups[1]) {
                "Student" -> {
                    val currentGroupName =
                        if (i % 2 == 0) randArrayElement(groupNames1)
                        else randArrayElement(groupNames2)

                    val currCourse = randInt(1, 4)
                    val currSemester = currCourse * randInt(1, 2)
                    val person = people.first()
                    val student =
                        Student(
                            id = UUID.randomUUID(),
                            name = person.firstName,
                            surname = person.lastName,
                            patronymic = person.fatherName,
                            login = person.login,
                            password = person.password,
                            groupName = currentGroupName,
                            course = currCourse.toString(),
                            semester = currSemester.toString()
                        )
                    studentList += student
                }
                "Teacher" -> {
                    val person = people.first()
                    val teacher =
                        Teacher(
                            id = UUID.randomUUID(),
                            name = person.firstName,
                            surname = person.lastName,
                            patronymic = person.fatherName,
                            login = person.login,
                            password = person.password,
                        )
                    teacherList += teacher
                }
                else -> {
                    println("Wrong user group")
                }
            }
            people.removeFirst()
        }
        createSubjects()

        for (group in groupNames1) {
            createScheduleForThreeWeeks(group)
        }
        for (group in groupNames2) {
            createScheduleForThreeWeeks(group)
        }

        createNotificationsForTwoMonths()
        createProfileAttendance()

        writeToJSON()
    }
}
