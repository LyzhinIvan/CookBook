from datetime import datetime


def log(filepath, title=None, **kwargs):
    with open(filepath, 'a') as f:
        f.write("***************************************\n")
        if title is not None:
            f.write(str(title) + '\n')
        f.write("***************************************\n")
        f.write(datetime.now().strftime("%Y-%m-%d %H:%M:%S") + ":\n")
        for arg_key in kwargs:
            f.write("---------------------------------------\n")
            f.write(str(arg_key) + '\n')
            if not isinstance(kwargs[arg_key], list):
                f.write(kwargs[arg_key])
                continue

             for item in kwargs[arg_key]:
                 f.write(str(item) + "\n")

        f.write("\n")
