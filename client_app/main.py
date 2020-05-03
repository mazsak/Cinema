import io

import eel
from PIL import Image
from zeep import Client

index_movie = None
index_screening = None
indexs_seats = []
index_reservation = None
pom_index = None
user = None

movie_Service = Client('http://localhost:9999/cinema/movieservice?wsdl').service
image_Service = Client('http://localhost:9999/cinema2/imageservice?wsdl').service
screening_Service = Client('http://localhost:9999/cinema3/screeningservice?wsdl').service
reservation_Service = Client('http://localhost:9999/cinema4/reservationservice?wsdl').service
user_Service = Client('http://localhost:9999/cinema5/userservice?wsdl').service

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
                       '<div class="flip-card" onclick="goToMovie(' \
                       + str(movie['id']) + \
                       ')">\n' \
                       '<div class="flip-card-inner">\n' \
                       '<div class="flip-card-front">\n' \
                       '<img src="' \
                       + path_image + \
                       '" style="width:230px;height:350px; border-radius: 10px 10px 0 0;">\n' \
                       '</div>\n' \
                       '<div class="flip-card-back">\n' \
                       '<h3>Duration: </h3>\n' \
                       '<p>' \
                       + str(movie['duration']) + \
                       '</p>\n' \
                       '<h3>Description: </h3>\n' \
                       '<p style="margin:1px;">' \
                       + movie['description'] + \
                       '</p>\n' \
                       '</div>\n' \
                       '</div>\n' \
                       '<div class="my-text" style="border-radius: 0 0 10px 10px;">\n' \
                       '<span >' \
                       + movie['title'] + \
                       '</span>\n' \
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
def change_index_reservation(id, screening_id):
    global index_reservation
    global index_screening
    index_screening = screening_id
    index_reservation = id


@eel.expose
def create_view_details_movie():
    movie = movie_Service.getMovieById(index_movie)
    if movie['imagePath'] == None:
        path_image = '/img/not-found.jpg'
    else:
        path_image = 'img/' + str(movie['id']) + '.jpg'

    string_html = '<div class="row" style="background-color: #343a40;">\n' \
                  '<div style="width: 300px;">\n' \
                  '<img id="image-movie" src="' \
                  + path_image + \
                  '" />\n' \
                  '</div>\n' \
                  '<div class="col" style="margin-top: 20px">\n' \
                  '<h1>' \
                  + movie['title'] + \
                  '</h1>\n' \
                  '<h5>' \
                  + movie['description'] + \
                  '</h5>\n' \
                  '<p style="margin-top: 20px">Duration: ' \
                  + str(movie['duration']) + \
                  '</p>\n' \
                  '</div>\n' \
                  '<div class="col"></div>' \
                  '</div>\n' \
                  '<div class="row justify-content-center" style="margin-top: 30px;">\n' \
                  '<div class="col-5" style="background-color: #343a40; margin-right: 5px;">\n' \
                  '<h4 style="text-align: center">Actors</h4>\n' \
                  '<table class="table table-striped table-dark" style="padding: 20px">\n'

    for actor in movie['actors']:
        string_html += '<tr>\n' \
                       '<td>' \
                       + actor['firstName'] + \
                       '</td>\n' \
                       '<td>' \
                       + actor['secondName'] + \
                       '</td>\n' \
                       '</tr>\n'

    string_html += '</table>\n' \
                   '</div>\n' \
                   '<div class="col-5" style="background-color: #343a40; margin-left: 5px;">\n' \
                   '<h4 style="text-align: center">Directors</h4>\n' \
                   '<table class="table table-striped table-dark" style="padding: 20px">\n'

    for director in movie['directors']:
        string_html += '<tr>\n<td>' \
                       + director['firstName'] + \
                       '</td>\n' \
                       '<td>' \
                       + director['firstName'] + \
                       '</td>\n' \
                       '</tr>\n'

    string_html += '</table>\n' \
                   '</div>\n' \
                   '</div>\n'

    return string_html


@eel.expose
def create_view_repertoire(day, month, year):
    screenings = screening_Service.getScreeningsByDate(year, month, day)

    month_name = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']

    string_html = '<h2 style="text-align: center;">' + str(day) + ' ' + month_name[month - 1] + '</h2>'
    if screenings:
        auditoriums = {}
        for screening in screenings:
            if auditoriums.get(screening['auditorium']['id'], None) is None:
                auditoriums[screening['auditorium']['id']] = {'name': screening['auditorium']['name'], 'movie': {}}
            if auditoriums[screening['auditorium']['id']]['movie'].get(screening['movie']['id'], None) is None:
                auditoriums[screening['auditorium']['id']]['movie'][screening['movie']['id']] = {
                    'id': screening['movie']['id'], 'title': screening['movie']['title'],
                    'description': screening['movie']['description'], 'imagePath': screening['movie']['imagePath'],
                    'screenings': []}
            auditoriums[screening['auditorium']['id']]['movie'][screening['movie']['id']]['screenings'].append(
                screening)

        for key, auditorium in auditoriums.items():
            string_html += '<div style="background-color: #343a40; width: 100%; margin-top: 20px; padding-bottom: 20px">\n' \
                           '<div style="text-align: center">\n' \
                           '<h3>' \
                           + auditorium['name'] + \
                           '</h3>\n' \
                           '</div>\n' \
                           '<div>\n' \
                           '<table class="table table-striped table-dark" style="width: 90%; margin-right: auto; margin-left: auto; margin-bottom: 30px">\n'

            for key, movie in auditorium['movie'].items():
                if movie['imagePath'] is None:
                    path_image = '/img/not-found.jpg'
                else:
                    bytes = image_Service.getImage(movie['imagePath'])
                    image = Image.open(io.BytesIO(bytes))
                    image.save('pages/img/' + str(movie['id']) + '.jpg')
                    path_image = 'img/' + str(movie['id']) + '.jpg'
                string_html += '<tr>\n' \
                               '<td style="width: 300px;">\n' \
                               '<img id="image-movie" src="' \
                               + path_image + \
                               '">\n' \
                               '</td>\n' \
                               '<td>\n' \
                               '<div style="margin-top: 20px; height: 350px; text-align: left">\n' \
                               '<h1>' \
                               + movie['title'] + \
                               '</h1>\n' \
                               '<h5>' \
                               + movie['description'] + \
                               '</h5>\n' \
                               '<div class="row">\n'

                for screening in movie['screenings']:
                    string_html += '<div class="card-time" onclick="goToReservation(' \
                                   + str(screening['id']) + \
                                   ')">\n' \
                                   '<h6 >' \
                                   + str(screening['hour']) + \
                                   ':' \
                                   + str(screening['minutes']) + \
                                   '</h6>\n' \
                                   '</div>\n'

                string_html += '</div>\n' \
                               '</div>\n' \
                               '</td>\n' \
                               '</tr>\n'

            string_html += '</table>\n' \
                           '</div>\n' \
                           '</div>'

    return string_html


@eel.expose
def change_index_screening(id):
    global index_screening
    index_screening = id


@eel.expose
def create_view_reservation():
    global index_reservation
    global indexs_seats
    global pom_index
    pom_index = index_reservation
    index_reservation = None
    if pom_index is not None:
        reservation = reservation_Service.findReservationById(pom_index)
    indexs_seats = []
    screening = screening_Service.getScreeningById(index_screening)
    seats = reservation_Service.findReservedSeatsByScreeningId(index_screening)
    string_html = '<div id="seats" class="col-5" style="background-color: #343a40; margin: 10px; padding: 20px">\n' \
                  '<table class="justify-content-center">\n' \
                  '<tr>\n' \
                  '<td></td>\n' \
                  '<td>\n' \
                  '<div style="width: 100%; background-color: gray; text-align: center">\n' \
                  '<h5>Screen</h5>\n' \
                  '</div>\n' \
                  '</td>\n' \
                  '</tr>\n'
    i = 1
    y = 1
    for index_row, row in enumerate(seats):
        string_html += '<tr>\n' \
                       '<td>\n' \
                       '<h6 class="card-seat">\n' \
                       + str(i) + \
                       '</h6>\n' \
                       '</td>\n' \
                       '<td>\n' \
                       '<div>\n'
        for index_number, seat in enumerate(row['item']):
            id_seat = \
                [id['id'] for id in screening['auditorium']['seats'] if id['number'] == y - 1 and id['row'] == i - 1]
            id_seat = id_seat[0]
            string_html += '<div id="' \
                           + str(id_seat) + \
                           '" class="card-seat"\n'
            if pom_index is not None and [reservation_seat for reservation_seat in reservation['seats'] if
                                                  reservation_seat['row'] == index_row and reservation_seat[
                                                      'number'] == index_number]:
                indexs_seats.append(id_seat)
                string_html += 'style=" background-color: blue; color: white; cursor: pointer; "\n'
            elif seat:
                string_html += 'style=" background-color: indianred; color: white; cursor: pointer; "\n'
            else:
                string_html += 'style=" background-color: white; color: black; cursor: pointer; "\n'
            string_html += 'onclick="reservate(' \
                           + str(id_seat) + \
                           ')">\n' \
                           '<h6>\n' \
                           + str(y) + \
                           '</h6>\n' \
                           '</div>\n'
            y += 1
        i += 1
        y = 1
        string_html += '</div>\n' \
                       '</td>\n' \
                       '</tr>\n'
    string_html += '</table>\n' \
                   '</div>\n' \
                   '<div class="col-2" style="background-color: #343a40; margin: 10px; padding: 10px;">\n' \
                   '<div>Title: ' \
                   + screening['movie']['title'] + \
                   '</div>\n' \
                   '<div>Duration:' \
                   + str(screening['movie']['duration']) + \
                   '</div>\n' \
                   '<div>Date: ' \
                   + str(screening['day']) + '.' + str(screening['month']) + '.' + str(screening['year']) + \
                   '</div>\n' \
                   '<div>Time: ' \
                   + screening['hour'] + ':' + screening['minutes'] + \
                   '</div>\n' \
                   '<button id="book-now" type="button" class="btn btn-light" style="margin-top:50px;" onclick="goToConfirmation()" disabled>Book now</button>\n' \
                   '</div>\n' \
                   '</div>'
    return string_html


@eel.expose
def add_index_seats(id):
    indexs_seats.append(id)


@eel.expose
def remove_index_seats(id):
    indexs_seats.remove(id)

@eel.expose
def remove_reservation(id):
    reservation_Service.cancelReservation(id)


@eel.expose
def reserve():
    global pom_index
    if pom_index is None:
        reservation_Service.reserve(index_screening, indexs_seats, user['id'])
    else:
        reservation_Service.updateSeats(pom_index, indexs_seats)
        pom_index = None


@eel.expose
def login(login, password):
    global user
    user = user_Service.findUserByUsernameAndPassword(login, password)
    if user is None:
        return "Error"
    else:
        return "Exist"


@eel.expose
def check_user():
    if user is None:
        return 'Empty'
    else:
        return '<h5>' + user['username'] + '</h5>'


@eel.expose
def get_email():
    return user['mail']


@eel.expose
def register(username, password, phoneNumber, mail):
    global user
    user = user_Service.createUser(username, password, phoneNumber, mail)
    if user is None:
        return "Error"
    else:
        return "Exist"


@eel.expose
def create_view_list_reservation():
    reservations = reservation_Service.getReservationByUserId(user['id'])

    string_html = '<h3 class="text-center">Reservations</h3>\n' \
                  '<table class="table table-striped table-dark"' \
                  'style="width: 90%; margin-right: auto; margin-left: auto; margin-bottom: 30px">\n'
    for reservation in reservations:
        if reservation['screening']['movie']['imagePath'] is None:
            path_image = '/img/not-found.jpg'
        else:
            bytes = image_Service.getImage(reservation['screening']['movie']['imagePath'])
            image = Image.open(io.BytesIO(bytes))
            image.save('pages/img/' + str(reservation['screening']['movie']['id']) + '.jpg')
            path_image = 'img/' + str(reservation['screening']['movie']['id']) + '.jpg'
        string_html += '<tr>\n' \
                       '<td style="width: 300px;">\n' \
                       '<img id="image-movie" src="' \
                       + path_image + \
                       '">\n' \
                       '</td>\n' \
                       '<td>\n' \
                       '<div style="margin-top: 20px; height: 350px; text-align: left">\n' \
                       '<h1>' \
                       + reservation['screening']['movie']['title'] + \
                       '</h1>\n' \
                       '<h5>' \
                       + reservation['screening']['movie']['description'] + \
                       '</h5>\n' \
                       '<h6 style="margin-top: 20px">Time: ' \
                       + reservation['screening']['hour'] + \
                       ':' \
                       + reservation['screening']['minutes'] + \
                       '</h6>\n' \
                       '<h6 style="margin-top: 20px">Places: '
        for seat in reservation['seats']:
            string_html += '<p> Row: ' \
                           + str(seat['row'] + 1) + \
                           ', Number: ' \
                           + str(seat['number'] + 1) + \
                           '</p>'
        string_html += '</h6>\n' \
                       '</div>\n' \
                       '</td>\n' \
                       '<td class="align-middle">\n' \
                       '<button type="button" class="btn btn-light" onclick="edit(' \
                       + str(reservation['id']) + \
                       ',' \
                       + str(reservation['screening']['id']) + \
                       ')">\n' \
                       'Edit\n' \
                       '</button>\n' \
                       '</td>\n' \
                       '<td class="align-middle">\n' \
                       '<button type="button" class="btn btn-light" onclick="remove(' \
                       + str(reservation['id']) + \
                       ')">\n' \
                       'Delete\n' \
                       '</button>\n' \
                       '</td>\n' \
                       '</tr>\n'
    string_html += '</table>'
    return string_html


@eel.expose
def is_user_logged_in():
    if user is None:
        eel.show('login.html')
        # return 'window.location.href = "login.html";'


eel.start('login.html')
