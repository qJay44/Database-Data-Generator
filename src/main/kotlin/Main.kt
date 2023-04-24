fun main() {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    with(DatabaseDataBuilder) {
        createAll()

        println("Students: ${studentList.size}")
        println("Teachers: ${teacherList.size}")
        println("Schedules: ${scheduleList.size}")
        println("Notifications: ${notificationList.size}")
        println("Profile attendances: ${profileAttendanceList.size}")
        println("Subjects: ${subject1List.size + subject2List.size}")
    }
}