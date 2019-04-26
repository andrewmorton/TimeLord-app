(ns timelord_web.page_data 
  (:require [hiccup.page :as hWrite]
            [logging_interface.log :as log])
  (:gen-class))

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
  ([element-label element-name {:keys [break]}]

   (if (= true break)
    (hWrite/html5
      [:label {:for element-name} element-label] [:br]
      [:input {:id element-name :type "text" :name element-name}] [:br])
    (hWrite/html5 
      [:label {:for element-name} element-label]
      [:input {:id element-name :type "text" :name element-name}])))

  ([element-label element-name]
   (create-input-field element-label element-name {:break false})))
  


;;============================================
;;Login Page

;;username error div for login
(defn username-error
  "creates an error div to attach to login screen."
  []
  (hWrite/html5
    [:div.loginErrors
     [:p "TimeLord encountered an error with the username provided, please verify the following:"]
     [:ul
      [:li "Username must be at least 4 characters"]
      [:li "Username must not contain spaces"]
      [:li "Username cannot contain special characters"]]]))


;;password error div for login
(defn password-error
  "creates an error div to attach to login screen."
  []
  (hWrite/html5
    [:div.loginErrors
     [:p "TimeLord encountered an error with the password provided, please verify the following:"]
     [:ul
      [:li "Password must be at least 6 characters."]
      [:li "Password must contain at least 1 special character."]]]))



;creates the login page
(defn login
  "login page for timelord.
  Error is only to append errors to login page."
  ([error]

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
          [:label {:for "userName"} "Username"][:br]
          [:input#userName {:type "text" :name "userName" :readOnly "true"}][:br]
          [:label {:for "password"} "Password"][:br]
          [:input#password {:type "password" :name "password" :readOnly "true"}][:br]
          [:input#login {:type "submit"}]]]
        error])))
  ([]
   (login "")))

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

(defn tracker-form-fields
  "creates the form fields for the tracker form"
  []
  (let [contact-input (create-input-field "Contact Name: " "contactName")
        problem-input (create-input-field "Problem: " "problem")
        remote-input (create-input-field "Remote: " "remote")
        environment-input  (create-input-field "Environment: " "environment")]
    (hWrite/html5
      [:div#trackerFields
       contact-input
       problem-input
       remote-input
       environment-input])))

(defn tracker-form-textareas
  "Creates the text Areas for the tracker form."
  []
  (hWrite/html5
    [:div#trackerTextareas
       [:br]
       [:label {:for "actions"} "Actions Taken: "][:br]
       [:textarea#textActions {:form "notesForm" :name "actions" :rows "10" :cols "50"}][:br]
       [:label {:for "nextSteps"} "Next Steps: "]
       [:textarea#textNextSteps {:form "notesForm" :name "nextSteps" :rows "10" :cols "50"}][:br]
       [:input#reset {:type "reset" :value "Clear Form" :onclick "javascript:resetTextAreas();"}]
       [:input#save {:type "submit" :value "save"}]]))





(defn tracker-form
  "Creates the form for the tracker page"
  []
  (let [fields (tracker-form-fields)
        text-areas (tracker-form-textareas)]

    (log/info ::tracker-form "Serving Tracker Form to user." {:metric 1 :tags ['app 'form 'pages 'http]})
    (hWrite/html5
      [:div#tracker-form
       [:form#tracker-form {:action "/submit-form" :method "POST"}
         fields
         text-areas]])))


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
              [:h1 "Notes and Time Tracking"][:br]
              [:p "Please note, this is still in development and only has"[:br]
               "basic functionality"]]
             (tracker-time-stamps)
             (tracker-form)])))

