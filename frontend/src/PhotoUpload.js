import { Component } from "react";
import { Form, FormGroup, FormText, Label, Input, Button } from "reactstrap";
import { Navigate } from "react-router-dom";
import AppNavbar from "./AppNavbar";

class PhotoUpload extends Component {
  constructor(props) {
    super(props);
    this.state = { isSuccess: false };
    this.onSubmit = this.onSubmit.bind(this);
  }

  async onSubmit(event) {
    event.preventDefault();
    let formData = new FormData();
    formData.append("data", document.getElementById("fileUpload").files[0]);
    await fetch("api/photos", {
      method: "POST",
      body: formData,
    })
      .then(() => {
        this.setState({ isSuccess: true });
      })
      .catch((error) => {
        console.log(error);
        this.setState({ isSuccess: false });
      });
  }

  render() {
    const { isSuccess } = this.state;
    // Redirect to home page on success
    if (isSuccess) {
      return (
        <>
          <AppNavbar />
          <p>Loading...</p>
          <Navigate to="/" replace={true}></Navigate>
        </>
      );
    }
    return (
      <>
        <AppNavbar />
        <Form onSubmit={this.onSubmit}>
          <FormGroup>
            <Label for="fileUpload">Upload Image</Label>
            <Input id="fileUpload" name="file" type="file" />
            <FormText>Please upload an image file</FormText>
          </FormGroup>
          <Button color="primary" type="submit">
            Submit
          </Button>
        </Form>
      </>
    );
  }
}

export default PhotoUpload;
