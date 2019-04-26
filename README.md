# TimeLord

A web application to keep track of notes and time when working on Cases in an office or productivity environment.
--This is something that I started just as a project to learn basics of Coding :)


This is a placeholder Readme since the repo is currently private. However the basics are:
    TimeLord is a way for offices/employees/coworkers to keep track of the amount of time that they have spent working
    on cases, tickets, and activities in the workplace. Currently, it is just a login page that will, on validation,
    drop the user to a tracker page which will be the main focus of the app. Current feature wish-list:

        1. Points system based on submitting a "ticket" and the amount of time that was logged for that ticket
        2. CSS and styling to make the application look....not as plain!
        3. A DB interface to store all that tasty data, along with a few tables
        4. A text-based "alerts" box on the tracker page that will update with information based on user activity
        5. An updating table on the tracker page that will allow users to see where they stack up against other users
        6. Support for crowning a "TimeLord" at the end of each day based on who has logged the most time
        7. A user "store" where they can spend Points to interact with other users

There will be others as TimeLord is developed, but the above are the major features that I want to implement.
Clojure is currently the language that most of the application is written in, as well as CSS, HTML, and JavaScript
for the UI (Once I get there that is).

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headless

## License
--haven't looked into this yet
Copyright © 2019 Andrew Morton
