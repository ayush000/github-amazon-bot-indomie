(ns github-amazon-bot.core
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]
            [morse.api :as api]
            [cheshire.core :as cc]))

(defn get-site-body
  "Get website body"
  [url]
  (:body (client/get url)))

(defn is-text-present?
  "Check if text is present in passed string"
  [text-to-search contents]
  (let [pattern (re-pattern (str "(?im)" text-to-search))
        first-occurence (re-find pattern contents)]
    (boolean first-occurence)))

(defn get-items-to-watch
  "Get list of items to watch from a file"
  []
  (let [file-contents (slurp "shopping_list.json")]
    (cc/parse-string file-contents true)))

(defn -main
  "Main function"
  []
  (let [items (get-items-to-watch)
        text-to-search "currently unavailable"
        token (env :telegram-token)
        chat-id (env :telegram-chat-id)]
    (doseq [item items :let [name (item :name)
                             url (item :url)
                             body (get-site-body url)
                             item-is-available (not (is-text-present? text-to-search body))]]
      (when item-is-available
        (api/send-text token chat-id (str name " is available! Please buy it from " url))))))

