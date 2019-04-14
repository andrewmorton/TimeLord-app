(ns timelord_web.page_data 
  (:require [hiccup.page :as hWrite]))

;;Auxiliary functions to build elements


;;defines a function that returns a simple head/meta tags section, the main styling css, and allows a title for the page to be declared
(defn simple-header
  [title]
  (hWrite/html5
    [:title (str title)]
    [:meta {:charset "UTF-8"}]
    [:meta {:name "description" :content "App for recording tickets and time worked on a task"}]
    [:meta {:name "author" :content "Andrew Morton"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]))
        

;;Aux Functions:
(defn create-input-field
  "Creates an input field using the given parameters."
  ([element-name element-type {:keys [break]}]

   (if (= true break)
    (hWrite/html5
      [:label {:for element-name} element-name]
      [:br]
      [:input {:id element-name :type element-type :name element-name}]
      [:br])
    (hWrite/html5 
      [:label {:for element-name} element-name]
      [:input {:id element-name :type element-type :name element-name}])))

  ([element-name element-type]
   (create-input-field element-name element-type {:break false}))
    
  ([element-name]
   (create-input-field element-name "text" {:break false})))
  


;;============================================
;;Login Page
;creates the login page
(defn login
  "login page for timelord.
  Error is only to append errors to login page."
  ([& error]

   (let [login-header (simple-header "Login Page")
         login-css (hWrite/include-css "/CSS/login.css" "/CSS/mainStyling.css")
         login-js (hWrite/html5 [:script {:src "/JS/login.js" :defer "true"}])
         error error]
     (hWrite/html5
       ;http content for the page
       [:head
        login-header
        login-css
        login-js]

       [:body
         [:div#loginBoxForm
           [:img {:src ""}]
           [:h2 "Login: "]]

         [:div.loginErrors
           [:span.loginErrorSpan {:style "display: hidden"}
             [:ul]]]

         [:div
           [:form#loginForm.fullWidth {:action "/check-login" :method "POST" :readOnly "true"}
             [:label {:for "userName"} "Username"]
             [:br]
             [:input#userName {:type "text" :name "userName" :readOnly "true"}]
             [:br]
             [:label {:for "password"} "Password"]
             [:br]
             [:input#password {:type "password" :name "password" :readOnly "true"}]
             [:br]
             [:input#login {:type "submit"}]]]
        error])))

 ([]
  (login "")))

;;password error div for login
(def password-error
  "creates an error div to attach to login screen."
  (hWrite/html5
    [:div.loginErrors
     [:p "TimeLord encountered an error with the password provided, please verify the following:"]
     [:ul
      [:li "Password must be at least 6 characters."]
      [:li "Password must contain at least 1 special character."]]]))

;;username error div for login
(def username-error
  "creates an error div to attach to login screen."
  (hWrite/html5
    [:div.loginErrors
     [:p "TimeLord encountered an error with the username provided, please verify the following:"]
     [:ul
      [:li "Username must be at least 4 characters"]
      [:li "Username must not contain spaces"]
      [:li "Username cannot contain special characters"]]]))




;;================================================
;;Tracker page

(defn tracker-time-stamps
  "Creates the time stamps div for the tracker page"
  []
  (hWrite/html5
    [:div.timeStamps.fullWidth.high10
      [:div#startTimeStamp
        [:p "Start Time: "]]

      [:div#endTimeStamp
        [:p "End Time: "]]

      [:div#totalTime
        [:p "Total (mins): "]]]))

(defn tracker-form
  "Creates the form for the tracker page"
  []
  (hWrite/html5
    [:div#notesAndTimeForm
      [:form#notesAndTimeForm
        (create-input-field "contactName")
        (create-input-field "problem")
        (create-input-field "remote")
        (create-input-field "environment")
        [:br]
        [:label {:for "actions"} "Actions Taken: "]
        [:br]
        [:textarea#textActions {:form "notesForm" :name "actions" :rows "10" :cols "50"} "Actions Taken: "]
        [:br]
        [:label {:for "nextSteps"} "Next Steps: "]
        [:textarea#textNextSteps {:form "notesForm" :name "nextSteps" :rows "10" :cols "50"} "Next Steps: "]
        [:br]
        [:input#reset {:type "reset" :value "Clear Form"}]
        [:input#save {:type "submit" :value "save"}]]]))


(defn tracker
  "time tracking page for TimeLord"
    [user]
    (let [tracker-heading (simple-header "Tracker")
          tracker-css     (hWrite/include-css "/CSS/mainStyling.css" "/CSS/notesAndTime.css")
          tracker-js      (hWrite/html5 [:script {:src "/JS/tracker.js" :defer "true"}])]

         (hWrite/html5
           [:head
            tracker-heading
            tracker-css
            tracker-js]

           [:body
             [:div#title.fullWidth
              [:h1 (str "Welcome " user)]
              [:h1 "Notes and Time Tracking"]
              [:br]
              [:p "Please note, this is still in development and only has"
               [:br]
               "basic functionality"]]

             (tracker-time-stamps)
             (tracker-form)])))

