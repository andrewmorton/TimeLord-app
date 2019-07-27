(ns timelord_web.page_data 
  (:require [hiccup.page :as page*]
            [hiccup.form :as form*]
            [logging_interface.log :as log])
  (:gen-class))

;;Auxiliary functions to build elements
;;=====================================
;;======= Input field builders ========
;;=====================================

(def basic-input-f
  "Creates a basic input field"
  (fn [type name]
    (seq [[:label {:for name} (str name ": ")]
          [:input {:id (str name "-input-field") :type type :name name}]])))

(def break-input-f
  "Creates a basic input field with breaks in between the label and input fields."
  (fn [type name]
    (seq [[:label {:for name} (str name ": ")] [:br]
          [:input {:id (str name "-input-field") :type type :name name}] [:br]])))

(defn new-input
  [type]
  (fn [name]
    (basic-input-f type name)))

(defn new-input-break
  [type]
  (fn [name]
    (break-input-f type name)))
;;=====================================
;;=== Convenient input functions ======
;;=====================================

;;=====================================
;;======    wrapper functions    ======
;;=====================================

;;Aux Functions:
;;defines a function that returns a simple head/meta tags section, the main styling css, and allows a title for the page to be declared
(defn simple-header
  "Creates a basic header for most pages."
  [title]
  [:head
   [:title (str title)]
   [:meta {:charset "UTF-8"}]
   [:meta {:name "description" :content "App for recording tickets and time worked on a task"}]
   [:meta {:name "author" :content "Andrew Morton"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]])

(defn create-input-field
  "Creates an input field using the given parameters."
  ([element-label element-name {:keys [break]}]

   (if (= true break)
    (page*/html5
      [:label {:for element-name} element-label] [:br]
      [:input {:id element-name :type "text" :name element-name}] [:br])
    (page*/html5
      [:label {:for element-name} element-label]
      [:input {:id element-name :type "text" :name element-name}])))

  ([element-label element-name]
   (create-input-field element-label element-name {:break false})))



;;============================================
;;Login Page

;;username error div for login
(def username-error
  "creates an error div to attach to login screen."
    [:div.loginErrors
     [:p "TimeLord encountered an error with the username provided, please verify the following:"]
     [:ul
      [:li "Username must be at least 4 characters"]
      [:li "Username must not contain spaces"]
      [:li "Username cannot contain special characters"]]])


;;password error div for login
(def password-error
  "creates an error div to attach to login screen."
    [:div.loginErrors
     [:p "TimeLord encountered an error with the password provided, please verify the following:"]
     [:ul
      [:li "Password must be at least 6 characters."]
      [:li "Password must contain at least 1 special character."]]])

;;error div for an invalid password
(defn incorrect-password
  "Creates an error div to attach to login screen when a password entered is wrong."
  []
  [:div.loginErrors
   [:p "Invalid password entered, please try again."]])

;creates the login page

(defn login
  "login page for timelord.
  Error is only to append errors to login page."
  []
  (let [login-header (simple-header "Login")
        login-css (page*/include-css "/CSS/login.css" "/CSS/mainStyling.css")
        login-js (page*/html5 [:script {:src "/JS/login.js" :defer "true"}])]
    ;http content for the page
    (page*/html5
      [:head login-header login-css login-js]
      [:body
       [:div#login-div
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
         [:input#login {:type "submit" :value "Login"}]]]])))

;;================================================
;;new user page

(defn new-user-page
  "Creates a new user page"
  [username]
  (let [page-js [:script {:src "/JS/login.js" :defer "true"}]
        page-header (conj (simple-header "new-user-page") page-js)]
    (page*/html5
      page-header
      [:body
       [:div#new-user-form
        [:h1 "Create a new user for: " username]
        [:form {:action "/create-new-user/" :method "POST"}
         (form*/label "userName" "Username: ")
         (form*/text-field "userName" username) [:br]
         (form*/label "password" "Password: ")
         (form*/password-field "password") [:br]
         (form*/label "firstName" "First Name: ")
         (form*/text-field "firstName") [:br]
         (form*/label "lastName" "Last Name: ")
         (form*/text-field "lastName") [:br]
         (form*/submit-button "Submit")]]
       [:div#login-parameters
        [:h1 "Create a user account according to the following: "]
        [:ul#login-parameters-list
         [:h3 "Username MUST: "]
         [:li "Contain no special characters."]
         [:li "Be unique."]
         [:li "Be at least 6 characters long."]
         [:h3 "Password MUST: "]
         [:li "Be at least 6 characters."]
         [:li "Contain at least 1 special character."]]]])))



;;================================================
;;Tracker page

(defn tracker-time-stamps
  "Creates the time stamps div for the tracker page"
  []
  (page*/html5
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
      [:div#trackerFields
       contact-input
       problem-input
       remote-input
       environment-input]))

(defn tracker-form-textareas
  "Creates the text Areas for the tracker form."
  []
  [:div#trackerTextareas
   [:br]
   [:label {:for "actions"} "Actions Taken: "][:br]
   [:textarea#textActions {:form "notesForm" :name "actions" :rows "10" :cols "50"}][:br]
   [:label {:for "nextSteps"} "Next Steps: "]
   [:textarea#textNextSteps {:form "notesForm" :name "nextSteps" :rows "10" :cols "50"}][:br]
   [:input#reset {:type "reset" :value "Clear Form" :onclick "javascript:resetTextAreas();"}]
   [:input#save {:type "submit" :value "save"}]])

(defn tracker-form
  "Creates the form for the tracker page"
  []
  (log/info ::tracker-form "Serving Tracker Form to user." {:metric 1 :tags ['app 'form 'pages 'http]})
  [:div#tracker-form
   [:form#tracker-form {:action "/submit-form" :method "POST"}
     (tracker-form-fields)
     (tracker-form-textareas)]])


(defn tracker
  "time tracking page for TimeLord"
    [user]
    (let [tracker-heading (simple-header "Tracker")
          tracker-css     (page*/include-css "/CSS/mainStyling.css" "/CSS/notesAndTime.css")
          tracker-js      [:script {:src "/JS/tracker.js" :defer "true"}]]
         (page*/html5
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



(defn error-page
  "Page content for an error page. Takes error text from a throwable exception and spits it out into the page."
  [& errors]
  (let [header (page*/include-css "/CSS/mainSiteStyling.css")]
    (page*/html5
      [:head header]
      [:body
       [:div#error.error-div
        [:span errors]]
       [:form {:action "/login" :method "GET"}
        (form*/submit-button "Return to Login")]])))


