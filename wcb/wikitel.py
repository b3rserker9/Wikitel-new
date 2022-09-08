# coding=utf-8

import flask
from flask import Flask, request, render_template, json
from flask_restful import Resource, Api
import wikipediaapi
from nltk import tokenize
from nltk.corpus import stopwords
import it_core_news_sm
from six.moves.html_parser import HTMLParser
from flask import jsonify
import re
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer


app = Flask(__name__)
api = Api(app)

wiki = wikipediaapi.Wikipedia('it')
nlp = it_core_news_sm.load()

stop_words = set(stopwords.words('italian'))
stop_words.update(['.', ':', ',', ';', '-', '_',
                  '\'', '', '(', ')', '/', '!', '?'])

target_categories = {'Categoria:Archeologia', 'Categoria:Architettura', 'Categoria:Arte', 'Categoria:Geografia', 'Categoria:Economia', 'Categoria:Storia', 'Categoria:Geografia', 'Categoria:Scienza', 'Categoria:Biologia', 'Categoria:Agricoltura', 'Categoria:Antropologia', 'Categoria:Astronomia', 'Categoria:Botanica', 'Categoria:Branche_della_fisica', 'Categoria:Chiese_d%27Italia', 'Categoria:Chimica', 'Categoria:Cinema', 'Categoria:Cultura', 'Categoria:Elettronica',
                     'Categoria:Farmaci', 'Categoria:Geologia', 'Categoria:Industria', 'Categoria:Informatica', 'Categoria:Ingegneria', 'Categoria:Letteratura', 'Categoria:Medicina', 'Categoria:Musica', 'Categoria:Natura', 'Categoria:Opere_d%27arte', 'Categoria:Politica', 'Categoria:Psichiatria', 'Categoria:Psicologia', 'Categoria:Filosofia', 'Categoria:Religione', 'Categoria:Società', 'Categoria:Sociologia', 'Categoria:Software', 'Categoria:Statistica', 'Categoria:Storia', 'Categoria:Tecnologia'}
regexp = re.compile(
    '(Categoria:Senza fonti.*)|(Categoria:Contestualizzare fonti.*)|(Categoria:Informazioni senza fonte.*)|(Categoria:Errori del modulo citazione.*)|(Categoria:Collegamento interprogetto.*)|(Categoria:Categorie.*)|(Categoria:Template.*)|(Categoria:Pagine.*)|(Categoria:Voci.*)|(Categoria:Verificare.*)|(Categoria:P\d+)')


def get_category(title):
    cached_categories = set()       # monotonically encreasing
    q = list(wiki.page(title).categories)
    q.reverse()
    while (q):
        cat = q.pop()
        if cat in target_categories:
            return cat
        cats = list(wiki.page(cat).categories)
        cats.reverse()
        for parent in cats:
            if parent not in cached_categories and not regexp.search(parent):
                cached_categories.add(parent)
                q.append(parent)
    return None


class WikiTEL(Resource):
    def get(self):
        p = request.args.get('page')

        wiki_page = wiki.page(p)
        url = wiki_page.fullurl
        categories = set()
        preconditions = []

        for cat in wiki_page.categories:
            if regexp.search(cat):
                continue
            print('finding category for ' + cat)
            if cat in target_categories:
                categories.append(cat)
            else:
                c_cat = get_category(cat)
                if(c_cat):
                    categories.add(c_cat)

        doc = nlp(str(wiki_page.text))

        filtrati = []
        for w in doc:
            if w.text not in stop_words:
                filtrati.append(w.text)
                
        print('computing preconditions..')
        docs=[wiki_page.title]
        docs2=[str(wiki_page.text)]
        for s in wiki_page.links:
            preconditions.append(wiki.page(s).title)
            page_text = str(wiki.page(s).text)
            docs.append(page_text)
            docs2.append(page_text)

        tfidf_vectorizer = TfidfVectorizer()
        tfidf_matrix_train = tfidf_vectorizer.fit_transform(docs)  #finds the tfidf score with normalization
        tfidf_matrix_train2 = tfidf_vectorizer.fit_transform(docs2)

        print('computing rank1..')
        rank1=list(cosine_similarity(tfidf_matrix_train[0:1], tfidf_matrix_train)[0])
        print('computing rank2..')
        rank2=list(cosine_similarity(tfidf_matrix_train2[0:1], tfidf_matrix_train2)[0])

        rank1.pop(0)
        rank2.pop(0)
        return app.response_class(
            response=json.dumps(
                {'url': url,
                 'categories': list(categories),
                 'length': len(filtrati),
                 'preconditions': preconditions,
                 'rank1':rank1,
                 'rank2':rank2}),
            status=200,
            mimetype='application/json')


api.add_resource(WikiTEL, '/wiki')  # Route_1

if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5015')