(ns app.core
  (:require [clojure.edn :as edn]))

;;for uuid purposes
(import 'java.util.UUID)

;;collection creation
(defn create-collection
  "Create a new collection from string as a given name."
  [coll-name]
  (spit (str "resources/" coll-name ".edn") ""))

;;keperluan nih
(defn generateUUID
  []
  (str (UUID/randomUUID)))

(defn add-doc-to-collection
  "Add new doc with generated id to a collection."
  [coll-name doc]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        new-doc (assoc doc :id (generateUUID))]
    (spit coll-path (pr-str (conj existing-file new-doc))))
    )

(defn add-many-docs-to-collection
  "Add many docs (list of maps as the docs) with generated id to a collection."
  [coll-name docs]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        new-docs (map #(assoc % :id (generateUUID)) docs)]
    (spit coll-path (pr-str (concat existing-file new-docs))))
    )

;;read a doc based on id from a coll
(defn read-doc-from-coll
  [coll-name id]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        doc (some #(when (= (:id %) id) %) existing-file)]
    (if (nil? doc)
      "gaada doc-nya bos"
      doc)
    )
  )

;;read all doc
(defn read-all-doc-from-coll
  [coll-name]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))]
    existing-file
    ))
  

;;find doc(s) based on a query (as a map)
(defn find-docs-on-coll
  "Find doc(s) based on a query (as a map) from a collection."
  [coll-name query]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        filter-by-key-val (fn [docs key value]
                            (filter #(= (get % key) value) docs))
        found-docs (filter-by-key-val existing-file (first(keys query)) (first(vals query)))]
    (if (empty? found-docs)
      "gaada cuy"
      found-docs))
  )

;;update doc based on id
(defn update-doc
  "Update a doc in a collection based on id"
  [coll-name id coll-update]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        ;doc (some #(when (= (:id %) id) %) existing-file)
        updated-doc (some #(when (= (:id %) id) (merge % coll-update)) existing-file)
        updated-coll (merge (remove #(= (:id %) id) existing-file) updated-doc)] 
    (spit coll-path (pr-str updated-coll))
    ))

;;delete a doc based on id
(defn delete-doc
  "Delete a doc in a collection based on id"
  [coll-name id]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        updated-coll (remove #(= (:id %) id) existing-file)]
    (spit coll-path (pr-str updated-coll))
    ))


(defn delete-many-docs
  "Delete many docs in a collection based on list of ids"
  [coll-name ids]
  (let [coll-path (str "resources/" coll-name ".edn")
        existing-file (edn/read-string (slurp coll-path))
        updated-coll (filter #(not (some
                                      (fn [id]
                                        (= (:id %) id)) ids)) existing-file)]
    (spit coll-path (pr-str updated-coll))
    ))













