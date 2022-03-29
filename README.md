# USCRecSports

### Setup

* Run USCRecSports/uscrecsports.sql on your local machine/MySQL database

* Modify the JDBC username/password in the following files
    - USCRecSports/app/src/main/java/edu/usc/uscrecapp/ui/reservation/ReservationFragment.java
    - USCRecSports/app/src/main/java/edu/usc/uscrecapp/ui/home/HomeFragment.java
    - USCRecSports/app/src/main/java/edu/usc/uscrecapp/ui/notifications/NotificationsFragment.java

### Usage

* Run app on Pixel 3a API 30

* Login with the following credentials set (user, pass)
    - jjso, jjso
    - nhauptman, nhauptman
    - mwilson, mwilson
    - trojan, ttrojan

* Waiting List notifications are sent to the respective emails associated with the user accounts. To change this, the emails can be directly changed in the users table of sql file.

* App run in portrait mode
