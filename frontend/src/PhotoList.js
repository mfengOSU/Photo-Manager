import { Component } from "react";
import { Card, Button, CardImg, CardBody } from "reactstrap";

class PhotoList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      photos: [],
      isLoading: true,
    };
    this.deletePhoto = this.deletePhoto.bind(this);
  }

  componentDidMount() {
    fetch("/api/photos")
      .then((response) => response.json())
      .then((body) =>
        this.setState({
          photos: body["_embedded"]["photoList"],
          isLoading: false,
        })
      )
      .catch((error) => console.log(error));
  }

  async deletePhoto(id) {
    await fetch(`/api/photos/${id}`, { method: "DELETE" })
      .then(() => {
        const updatedPhotos = [...this.state.photos].filter(
          (photo) => photo.id !== id
        );
        this.setState({
          photos: updatedPhotos,
          isLoading: false,
        });
      })
      .catch((error) => console.log(error));
  }

  render() {
    const { photos, isLoading } = this.state;
    if (photos.length === 0) {
      return <></>;
    }
    if (isLoading) {
      return <p>Loading...</p>;
    }
    return (
      <div>
        {photos.map((photo) => (
          <Photo
            key={photo.id}
            photo={photo}
            onDelete={() => this.deletePhoto(photo.id)}
          ></Photo>
        ))}
      </div>
    );
  }
}

class Photo extends Component {
  constructor(props) {
    super(props);
    this.state = { imgUrl: "" };
  }

  componentDidMount() {
    const url = `/api/photos/${this.props.photo.id}/download`;
    fetch(url)
      .then(() => this.setState({ imgUrl: url }))
      .catch((error) => console.log(error));
  }

  render() {
    const { imgUrl } = this.state;
    if (imgUrl.length === 0) {
      return <></>;
    }
    return (
      <Card>
        <CardImg
          alt={`${this.props.photo.filename}`}
          src={imgUrl}
          width="300px"
          height="300px"
        ></CardImg>
        <CardBody>
          <Button color="danger" onClick={this.props.onDelete}>
            Delete
          </Button>
        </CardBody>
      </Card>
    );
  }
}

export default PhotoList;
