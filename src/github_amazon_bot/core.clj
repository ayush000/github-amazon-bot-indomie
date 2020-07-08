(ns github-amazon-bot.core
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]))

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
        body (get-site-body url)
        ipad-is-available (not (is-text-present? text-to-search body))]
    (println token)
    (println ipad-is-available)))

