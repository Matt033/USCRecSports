# USCRecSports

### Setup

* Run USCRecSports/uscrecsports.sql on your local machine/MySQL database

* Modify the JDBC username/password in the following files
    - USCRecSports/app/src/main/res/values/strings.xml

### Usage

* Run app on Pixel 3a API 30

* Login with the following credentials set (user, pass)
    - jjso, jjso
    - nhauptman, nhauptman
    - mwilson, mwilson
    - trojan, ttrojan

* Waiting List notifications are sent to the respective emails associated with the user accounts. To change this, the emails can be directly changed in the users table of sql file.

* App run in portrait mode


### Testing Instructions

* Navigate to USCRecSports/app/src/androidTest/java/edu/usc/uscrecapp (androidTest)
* Launch the Pixel 3a API 30 emulator
* Must update SQL credentials in the following tests
    - MultipleWaitlistNotificationVerification
    - WaitlistNotificationVerification
    - ManyReservationsHomePageTest
    - OneReservationHomePageTest
    - SummaryPreviousReservationVerification
    - SummaryUpcomingCancel
    - SummaryUpcomingReservationVerification
    - SummaryVerifyRebook
    - SummaryWaitingListVerification
    - SummaryWaitListRebook
    - WaitlistCancelVerification
* Right click on the following packages and select the “Run tests..” option
    - multipleNotificationTest
    - notificationTest
    - ui



