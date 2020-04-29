from typing import List

import eel
from zeep import Client
import json

index_movie = None

movie_Service = Client('http://localhost:9999/cinema/movieservice?wsdl').service

eel.init('pages')

@eel.expose
def create_view_list_movies():
    movies = movie_Service.getAllMovie()

    string_html = '<div class="row">\n'
    for index, movie in enumerate(movies):
        if index %5 == 0:
            string_html += '</div>\n<div class="row">\n'
        string_html += '<div class="col">\n' \
                         '<div class="flip-card" onclick="goToMovie(' + str(movie['id']) + ')">\n' \
                         '<div class="flip-card-inner">\n' \
                         '<div class="flip-card-front">\n' \
                         '<img src="img/iron.jpg" style="width:230px;height:350px; border-radius: 10px 10px 0 0;">\n' \
                         '</div>\n' \
                         '<div class="flip-card-back">\n' \
                         '<h3>Duration: </h3>\n' \
                         '<p>' + str(movie['duration']) + '</p>\n' \
                         '<h3>Description: </h3>\n' \
                         '<p style="margin:1px;">' + movie['description'] + '</p>\n' \
                         '</div>\n' \
                         '</div>\n' \
                         '<div class="my-text" style="border-radius: 0 0 10px 10px;">\n' \
                         '<span >' + movie['title'] + '</span>\n' \
                         '</div>\n' \
                         '</div>\n' \
                         '</div>\n'

    string_html+='</div>\n'
    return string_html

@eel.expose
def change_index_movie(id):
    global index_movie
    index_movie = id

@eel.expose
def create_view_details_movie():
    print(index_movie)

eel.start('index.html')