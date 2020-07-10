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
        num-chars (count first-occurence)]
    (> num-chars 0)))

(defn -main
  "Main function"
  []
  (let [ipads [{:url "https://www.amazon.in/dp/B0864JNFB8/" :name "iPad Pro 12.9"}
               {:url "https://www.amazon.in/Apple-iPad-11-inch-Wi-Fi-128GB/dp/B0864JKY83" :name "iPad Pro 11"}]
        text-to-search "currently unavailable"
        token (env :telegram-token)
        chat-id (env :telegram-chat-id)]
    (doseq [ipad ipads :let [name (ipad :name)
                             url (ipad :url)
                             body (get-site-body url)
                             ipad-is-available (not (is-text-present? text-to-search body))]]
      (when ipad-is-available
        (try
          (api/send-text token chat-id (str name " is available! Please buy it from " url))
          (catch Exception))))))

