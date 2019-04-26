(ns timelord_db.interface)

(defn random-int
  "Returns a random int from 1 to 10000.
  Should help reduce likelihood of functions preparing with the same name in PostgreSQL if put in front of the statement."
  []

  (int  (. Math floor (* 10000 (. Math random)))))

(defn select-user
  "selects a column for a test from db-pg."
  [[db value]]
  (let [db db
        value value
        statement (str "select_user" (random-int))]
    (try
      (jdbc/query db [(str "PREPARE " statement "(text)" "AS SELECT * FROM timelord_user WHERE username = $1;")])
      (catch Exception e (log/error ::select-user-prepare (str e) {:metric 1 :tags ['db 'select 'prepare 'try-block]})))

    (if-not Exception
      (do (try
            (jdbc/query db [(str "EXECUTE " statement "('" value "')")])
            (catch Exception e (log/error ::select-user-execute (str e) {:metric 1 :tags ['db 'execute 'try-block]})))
          (log/info ::select-user "User Select Successful" {:metric 1 :tags ['db 'execute 'try-block]}))
      (log/error ::select-user "Select-user returned an error." {:metric 1 :error Exception :tags ['db 'execute 'try-block]}))))








;;;;Info log on startup
(info ::db-interface "DB interface online." {:metric 0 :tags ['info 'status]})