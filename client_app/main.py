import io

import eel
from PIL import Image
from zeep import Client

index_movie = None

movie_Service = Client('http://localhost:9999/cinema/movieservice?wsdl').service
image_Service = Client('http://localhost:9999/cinema2/imageservice?wsdl').service
screening_Service = Client('http://localhost:9999/cinema3/screeningService?wsdl').service

eel.init('pages')


@eel.expose
def create_view_list_movies():
    movies = movie_Service.getAllMovie()

    string_html = '<div class="row">\n'
    for index, movie in enumerate(movies):
        if movie['imagePath'] == None:
            path_image = '/img/not-found.jpg'
        else:
            bytes = image_Service.getImage(movie['imagePath'])
            image = Image.open(io.BytesIO(bytes))
            image.save('pages/img/' + str(movie['id']) + '.jpg')
            path_image = 'img/' + str(movie['id']) + '.jpg'

        string_html += '<div class="col">\n' \
                       '<div class="flip-card" onclick="goToMovie(' + str(movie['id']) + ')">\n' \
                                                                                         '<div class="flip-card-inner">\n' \
                                                                                         '<div class="flip-card-front">\n' \
                                                                                         '<img src="' + path_image + '" style="width:230px;height:350px; border-radius: 10px 10px 0 0;">\n' \
                                                                                                                     '</div>\n' \
                                                                                                                     '<div class="flip-card-back">\n' \
                                                                                                                     '<h3>Duration: </h3>\n' \
                                                                                                                     '<p>' + str(
            movie['duration']) + '</p>\n' \
                                 '<h3>Description: </h3>\n' \
                                 '<p style="margin:1px;">' + movie['description'] + '</p>\n' \
                                                                                    '</div>\n' \
                                                                                    '</div>\n' \
                                                                                    '<div class="my-text" style="border-radius: 0 0 10px 10px;">\n' \
                                                                                    '<span >' + movie[
                           'title'] + '</span>\n' \
                                      '</div>\n' \
                                      '</div>\n' \
                                      '</div>\n'

    string_html += '</div>\n'
    return string_html


@eel.expose
def change_index_movie(id):
    global index_movie
    index_movie = id


@eel.expose
def create_view_details_movie():
    movie = movie_Service.getMovieById(index_movie)
    if movie['imagePath'] == None:
        path_image = '/img/not-found.jpg'
    else:
        path_image = 'img/' + str(movie['id']) + '.jpg'

    string_html = '<div class="row" style="background-color: #343a40;">\n' \
                  '<div style="width: 300px;">\n' \
                  '<img id="image-movie" src="' + path_image + '" />\n' \
                                                               '</div>\n' \
                                                               '<div class="col" style="margin-top: 20px">\n' \
                                                               '<h1>' + movie['title'] + '</h1>\n' \
                                                                                         '<h5>' + movie[
                      'description'] + '</h5>\n' \
                                       '<p style="margin-top: 20px">Duration: ' + str(movie['duration']) + '</p>\n' \
                                                                                                           '</div>\n' \
                                                                                                           '<div class="col"></div>' \
                                                                                                           '</div>\n' \
                                                                                                           '<div class="row justify-content-center" style="margin-top: 30px;">\n' \
                                                                                                           '<div class="col-5" style="background-color: #343a40; margin-right: 5px;">\n' \
                                                                                                           '<h4 style="text-align: center">Actors</h4>\n' \
                                                                                                           '<table class="table table-striped table-dark" style="padding: 20px">\n'
    for actor in movie['actors']:
        string_html += '<tr onclick="goToActor(' + str(actor['id']) + ')" style="cursor: pointer;">\n' \
                                                                      '<td>' + actor['firstName'] + '</td>\n' \
                                                                                                    '<td>' + actor[
                           'secondName'] + '</td>\n' \
                                           '</tr>\n'
    string_html += '</table>\n' \
                   '</div>\n' \
                   '<div class="col-5" style="background-color: #343a40; margin-left: 5px;">\n' \
                   '<h4 style="text-align: center">Directors</h4>\n' \
                   '<table class="table table-striped table-dark" style="padding: 20px">\n'
    for director in movie['directors']:
        string_html += '<tr onclick="goToDirector(' + str(director['id']) + ')" style="cursor: pointer;">\n<td>' + \
                       director['firstName'] + '</td>\n' \
                                               '<td>' + director['firstName'] + '</td>\n' \
                                                                                '</tr>\n'
    string_html += '</table>\n' \
                   '</div>\n' \
                   '</div>\n'
    return string_html

@eel.expose
def create_view_repertoire(day, month, year):
    screenings = screening_Service.getScreeningsByDate(year, month, day)
    movies_with_screenings = {}
    for screening in screenings:
        if movies_with_screenings.get(screening['auditorium']['id'], None) is None:
            movies_with_screenings[screening['auditorium']['id']] = {}
        if movies_with_screenings[screening['auditorium']['id']].get(screening['movie']['id'], None) is None:
            movies_with_screenings[screening['auditorium']['id']][screening['movie']['id']] = []
        movies_with_screenings[screening['auditorium']['id']][screening['movie']['id']].append(screening)

    for auditorium in movies_with_screenings:
        for movie in auditorium:
            for screening in movie:
                pass



    return '<h1>jestem</h1>'

eel.start('index.html')
