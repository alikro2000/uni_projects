import urllib.request
import json
import os, shutil


# Returns an array of urls from a JSON file at path.
#
# Throws an [Exception] if file wasn't found.
def load_urls_from_json(path):
    links_arr = []
    with open (path, 'r') as json_file:
        data = json.load(json_file)
        links_arr = data['urls']
    return links_arr

# Downloads contents of a page at url and saves it as a file.
#
# The page is saved to ./server_file/[url].html.
# [url] must be formatted like www.domain.top_dom without https:// prefix. For example ```www.python.org``` is good.
# Throws an exception if access to a given url is limited or if it's not found.
# Alos, throws an exception if the content's formatting is not compatible with utf-8.
def download_page_by_url(url):
    html_res = urllib.request.urlopen('https://{url}'.format(url = url))
    html_content = html_res.read()
    with open('server_file/{url}.html'.format(url = url), 'w', encoding='utf-8') as file:
        file.write(html_content.decode())

# Deletes contents of ./server_file folder.
#
# Calling this method results in all previously downloaded data to be deleted. Be careful when using it.
def delete_previous_data():
    for filename in os.listdir('./server_file'):
        file_path = os.path.join('./server_file', filename)
        try:
            if os.path.isfile(file_path) or os.path.islink(file_path):
                os.unlink(file_path)
            elif os.path.isdir(file_path):
                shutil.rmtree(file_path)
        except Exception as e:
            print('Failed to delete %s. Reason: %s' % (file_path, e))
    return 

# First, create the directory to keep downloaded pages' data if it doesn't exist Or delete previous data
if not os.path.exists('./server_file'):
    os.mkdir('./server_file')
print('Deleting previous data...')
delete_previous_data()

# Load urls from file and download the corresponding pages
urls_arr = load_urls_from_json('./info.json')
print('Downloading pages...')
for url in urls_arr:
    print('Downloading {url} | ...'.format(url = url), end='')
    download_page_by_url(url)
    print('\b\b\b✔  ')
