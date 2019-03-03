# PulsR
Android Developemnt

PulsR is a running companion app that provides users with training regimens that are imported onto a calendar. Unlike popular running apps such as Strava, RunKeeper, etc. where a user is left to their own devices in making progress, PulsR advises a user to run specified amounts on said calendar. This app is meaningful because we are trying to capture a coach and athlete relationship with the users. We are holding them accountable with the days that they’ve missed workouts, and reminding them about workouts that they have the morning of. Our intended goal is to not only motivate our users but also teach them how to schedule runs with ample recovery time. PulsR has an interface that keeps the users accountable and motivates them throughout the whole process. This running app incorporates liveness by sending live notifications that remind users to work out the day of. PulsR also tracks live data such as distance ran, duration of runs, calories burned and even dynamically changes the background of the app depending on the current weather.


<img src="https://github.com/shuaiL8/PulsR/blob/master/images/Screen-Shot-2018-12-11-at-11.52.17-AM.png" width="800">


<img src="https://github.com/shuaiL8/PulsR/blob/master/images/Screen-Shot-2018-12-11-at-11.52.24-AM.png" width="800">

<img src="https://github.com/shuaiL8/PulsR/blob/master/images/11543352421_.pic_hd.jpg" width="250">

Red suggests the group to go running using PulsR to track the activity and reveals the current day’s workout to run 3 miles in 30 mins, with an estimated calorie expenditure of 300 calories.
Upon getting ready, the group starts tracking the run. The middle panel shows the current progress of the intended distance and how much distance is left to reach it. The circular buttons below correspond with the colors on the progress graph: duration, distance, and calories.

APIs:
Google Fitness
Google Place
Google Awareness
Google Map

Libraries:
DecoView
wx.wheelview
LeonidsLib
jjoe64:graphview

Other Sources:
www.learn2crack.com
https://github.com/DeveloperBrothers/Custom-Calendar-View

Related acticles:
https://www.verywellfit.com/six-week-5k-training-schedule-2910850
https://www.fitnessmagazine.com/workout/running/5k/beginner-training-plan-6-weeks-to-a-5k/
https://www.halhigdon.com/training-programs/5k-training/novice-5k/
https://www.runnersworld.co.uk/training/six-week-beginner-5k-schedule


Tips:
If you don't have the database set up yet and want to skip login page, go to MainActivity line:145 (fragmentTransaction.add(R.id.content, new HomeFragment());) change new LoginFragment() to new HomeFragment().
