import "./App.css";
import React from "react";
import PhotoList from "./PhotoList";
import Navbar from "./AppNavbar";

class App extends React.Component {
  render() {
    return (
      <div className="App">
        <Navbar></Navbar>
        <header className="App-header">
          <PhotoList />
        </header>
      </div>
    );
  }
}

export default App;
