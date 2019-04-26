(ns timelord_db.schema)

(def schema
  {:timelord_user ["user_uuid" "user_key" "username" "password" "first_name" "last_name" 'date_created 'time_created 'last_login_date
                   'last_login_time]
   :user_points   ['user_uuid_fk 'points]
   :user_spending ['key 'username 'date_submitted 'time_submitted 'source 'purchase 'amount 'resulting_balance]
   :user_addition ['key 'username 'date_submitted 'time_submitted 'source 'amount 'resulting_balance 'note_key]
   :note_log      ['key 'username 'date_submitted 'time_submitted 'note_key]
   :note_content  ['key 'username 'start_time 'end_time 'total_minutes 'contact 'problem 'remote 'remote_method 'environment
                   'actions 'next_steps]
   :chat_content  ['key 'content]
   :chat_log      ['key 'chat_content_fk 'sender 'recipient 'date_sent 'time_sent]})

