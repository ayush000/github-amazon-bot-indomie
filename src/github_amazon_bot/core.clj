(ns github-amazon-bot.core
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]
            [morse.api :as api]))

(defn get-site-body
  "Get website body"
  [url]
  (:body (client/get url)))

(defn is-text-present?
  "Check if text is present in passed string"
  [text-to-search contents]
  (let [pattern (re-pattern (str "(?im)" text-to-search))
        first-occurence (re-find pattern contents)
        num-occurrences (count first-occurence)]
    (if (> num-occurrences 0) true false)))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (let [url "https://www.amazon.in/Apple-iPad-11-inch-Wi-Fi-128GB/dp/B0864JKY83"
        text-to-search "currently unavailable"
        token (env :telegram-token)
        chat-id (env :telegram-chat-id)
        body (get-site-body url)
        ipad-is-available (not (is-text-present? text-to-search body))]
    (if ipad-is-available (try (api/send-text token chat-id (str "Ipad is available! Please buy it from " url)) (catch Exception ex true)) true)))

