(ns compojure-experiments.handler
	(:require [compojure.core :refer :all]
						[compojure.route :as route]
						[ring.util.response :as response])
	(:require [clojure.tools.logging :as log])
	(:require [clojure.data.json :as json])
	(:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {}}) ; See `wcar` docstring for opts
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

;;TODO - move HTTP error handling out to try catch and wrap

(defroutes app-routes
	(GET "/" [] "Hello World")
	(GET "/things/:id" [id] 
		(do
			(def ^:dynamic val (wcar* (car/get id)))
			(if (nil? val)
				(response/status
					(response/response nil) 404)
				(response/status
					(response/content-type
						(response/response
							(json/write-str {:name val}))
								"application/json") 200))))
	(POST "/things" {body :body}
		(do
			(def ^:dynamic key (wcar* (car/incr "things")))
			(if (nil? key)
				(do
					(log/error "Failed to Increment Key")
					(response/status (response/response "") 500))
				(do
					(if
						(= 
							"OK" 
							(wcar* (car/set key
								(get (json/read-str (slurp body)) "name")))) ;TODO - need input validation
						(response/status
							(response/content-type
								(response/response
									(json/write-str {:key key}))
										"application/json") 201)
						(response/status
							(response/response nil) 500))))))
	(route/not-found "Not Found"))

(defn wrap-logger [handler]
	(fn [request]
		(do
			(log/info (get request :request-method) (get request :uri))
			(handler request))))

(def app
	(wrap-logger app-routes))
